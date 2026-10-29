package com.store.digital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.store.digital.client.catalog.CatalogClient;
import com.store.digital.client.catalog.ProductServiceResponse;
import com.store.digital.client.exchange.ExchangeRateClient;
import com.store.digital.client.exchange.ExchangeRateResponse;
import com.store.digital.client.inventory.InventoryClient;
import com.store.digital.client.inventory.InventoryServiceResponse;
import com.store.digital.config.ServiceProperties;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ProductSummaryServiceTest {

	@Test
	void combinesProviderResponsesAndConvertsPrice() {
		CatalogClient catalogClient = mock(CatalogClient.class);
		InventoryClient inventoryClient = mock(InventoryClient.class);
		ExchangeRateClient exchangeRateClient = mock(ExchangeRateClient.class);
		ServiceProperties properties = new ServiceProperties(
				new ServiceProperties.Catalog("http://catalog"),
				new ServiceProperties.Inventory("http://inventory"),
				new ServiceProperties.Exchange("http://exchange", "USD", "MXN"));
		ProductSummaryService service = new ProductSummaryService(
				catalogClient,
				inventoryClient,
				exchangeRateClient,
				Runnable::run,
				properties);

		when(catalogClient.getProduct("P-101")).thenReturn(new ProductServiceResponse(
				"P-101", "Laptop Gamer XYZ", "Descripción", "Computación", new BigDecimal("1200.00")));
		when(inventoryClient.getInventory("P-101")).thenReturn(new InventoryServiceResponse(
				"P-101", "IN_STOCK", 15, "Almacén norte", "2 días"));
		when(exchangeRateClient.getLatestRate()).thenReturn(new ExchangeRateResponse(
				BigDecimal.ONE, "USD", "2026-10-29", Map.of("MXN", new BigDecimal("17.00"))));

		var result = service.getProductSummary("P-101");

		assertEquals("P-101", result.productId());
		assertEquals("Laptop Gamer XYZ", result.title());
		assertEquals(new BigDecimal("1200.00"), result.priceUsd());
		assertEquals(new BigDecimal("20400.00"), result.priceMxn());
		assertEquals("IN_STOCK", result.stockStatus());
		assertEquals(15, result.availableUnits());
		assertEquals("Frankfurter External API", result.currencyConversionSource());
		assertEquals("SUCCESS", result.status());
		verify(catalogClient).getProduct("P-101");
		verify(inventoryClient).getInventory("P-101");
		verify(exchangeRateClient).getLatestRate();
	}
}
