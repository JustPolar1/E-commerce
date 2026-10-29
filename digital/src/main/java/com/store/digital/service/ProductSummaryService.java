package com.store.digital.service;

import com.store.digital.client.catalog.CatalogClient;
import com.store.digital.client.catalog.ProductServiceResponse;
import com.store.digital.client.exchange.ExchangeRateClient;
import com.store.digital.client.exchange.ExchangeRateResponse;
import com.store.digital.client.inventory.InventoryClient;
import com.store.digital.client.inventory.InventoryServiceResponse;
import com.store.digital.config.ServiceProperties;
import com.store.digital.dto.response.ProductSummaryResponse;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;
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

		CompletableFuture.allOf(productFuture, inventoryFuture, exchangeFuture).join();

		ProductServiceResponse product = productFuture.join();
		InventoryServiceResponse inventory = inventoryFuture.join();
		ExchangeRateResponse exchange = exchangeFuture.join();

		var conversionRate = exchange.rateFor(targetCurrency);
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
