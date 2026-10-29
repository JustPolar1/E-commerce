package com.store.digital.service;

import com.store.digital.client.catalog.CatalogClient;
import com.store.digital.client.catalog.ProductServiceResponse;
import com.store.digital.client.exchange.ExchangeRateClient;
import com.store.digital.client.exchange.ExchangeRateResponse;
import com.store.digital.client.inventory.InventoryClient;
import com.store.digital.client.inventory.InventoryServiceResponse;
import com.store.digital.config.ServiceProperties;
import com.store.digital.dto.response.ProductSummaryResponse;
import com.store.digital.exception.UpstreamServiceException;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import org.springframework.stereotype.Service;

@Service
public class ProductSummaryService {
	private static final String CONVERSION_SOURCE = "Frankfurter External API";

	private final CatalogClient catalogClient;
	private final InventoryClient inventoryClient;
	private final ExchangeRateClient exchangeRateClient;
	private final Executor executor;
	private final String targetCurrency;

	public ProductSummaryService(
			CatalogClient catalogClient,
			InventoryClient inventoryClient,
			ExchangeRateClient exchangeRateClient,
			Executor executor,
			ServiceProperties properties) {
		this.catalogClient = catalogClient;
		this.inventoryClient = inventoryClient;
		this.exchangeRateClient = exchangeRateClient;
		this.executor = executor;
		this.targetCurrency = properties.exchange().targetCurrency();
	}

	public ProductSummaryResponse getProductSummary(String productId) {
		CompletableFuture<ProductServiceResponse> productFuture = CompletableFuture.supplyAsync(
				() -> catalogClient.getProduct(productId), executor);
		CompletableFuture<InventoryServiceResponse> inventoryFuture = CompletableFuture.supplyAsync(
				() -> inventoryClient.getInventory(productId), executor);
		CompletableFuture<ExchangeRateResponse> exchangeFuture = CompletableFuture.supplyAsync(
				exchangeRateClient::getLatestRate, executor);

		try {
			CompletableFuture.allOf(productFuture, inventoryFuture, exchangeFuture).join();
		} catch (CompletionException exception) {
			Throwable cause = exception.getCause();
			if (cause instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}
			if (cause instanceof Error error) {
				throw error;
			}
			throw new UpstreamServiceException("No se pudieron consultar los servicios requeridos.", cause);
		}

		ProductServiceResponse product = productFuture.join();
		InventoryServiceResponse inventory = inventoryFuture.join();
		ExchangeRateResponse exchange = exchangeFuture.join();
		if (product == null || inventory == null || exchange == null || product.priceUsd() == null) {
			throw new UpstreamServiceException("Un servicio devolvió una respuesta incompleta.");
		}

		var conversionRate = exchange.rateFor(targetCurrency);
		if (conversionRate == null) {
			throw new UpstreamServiceException("La tasa de cambio solicitada no está disponible.");
		}
		var priceLocal = product.priceUsd().multiply(conversionRate).setScale(2, RoundingMode.HALF_UP);

		return new ProductSummaryResponse(
				product.productId(),
				product.title(),
				product.description(),
				product.category(),
				product.priceUsd(),
				priceLocal,
				inventory.stockStatus(),
				inventory.availableUnits(),
				inventory.warehouseLocation(),
				inventory.estimatedDelivery(),
				CONVERSION_SOURCE,
				"SUCCESS");
	}
}
