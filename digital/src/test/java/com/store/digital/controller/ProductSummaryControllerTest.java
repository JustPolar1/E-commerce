package com.store.digital.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.digital.client.catalog.CatalogClient;
import com.store.digital.client.catalog.ProductServiceResponse;
import com.store.digital.client.exchange.ExchangeRateClient;
import com.store.digital.client.exchange.ExchangeRateResponse;
import com.store.digital.client.inventory.InventoryClient;
import com.store.digital.client.inventory.InventoryServiceResponse;
import com.store.digital.exception.ProductNotFoundException;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductSummaryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CatalogClient catalogClient;

	@MockitoBean
	private InventoryClient inventoryClient;

	@MockitoBean
	private ExchangeRateClient exchangeRateClient;

	@Test
	void returnsUnifiedSummary() throws Exception {
		givenProviderResponsesForProduct("P-101");

		mockMvc.perform(get("/api/v1/products/P-101/summary"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.product_id").value("P-101"))
				.andExpect(jsonPath("$.price_mxn").value(20400.00))
				.andExpect(jsonPath("$.stock_status").value("IN_STOCK"))
				.andExpect(jsonPath("$.status").value("SUCCESS"));
	}

	@Test
	void returnsNotFoundWhenCatalogDoesNotFindProduct() throws Exception {
		when(catalogClient.getProduct("P-404"))
				.thenThrow(new ProductNotFoundException("P-404"));

		mockMvc.perform(get("/api/v1/products/P-404/summary"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("No se encontró el producto solicitado."));
	}

	@Test
	void returnsServiceUnavailableWhenAProviderCannotBeReached() throws Exception {
		givenProviderResponsesForProduct("P-101");
		when(exchangeRateClient.getLatestRate())
				.thenThrow(new ResourceAccessException("connection refused"));

		mockMvc.perform(get("/api/v1/products/P-101/summary"))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(503))
				.andExpect(jsonPath("$.path").value("/api/v1/products/P-101/summary"));
	}

	@Test
	void rejectsInvalidProductId() throws Exception {
		mockMvc.perform(get("/api/v1/products/bad%24id/summary"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	private void givenProviderResponsesForProduct(String productId) {
		when(catalogClient.getProduct(productId)).thenReturn(new ProductServiceResponse(
				productId, "Laptop Gamer XYZ", "Descripción", "Computación", new BigDecimal("1200.00")));
		when(inventoryClient.getInventory(productId)).thenReturn(new InventoryServiceResponse(
				productId, "IN_STOCK", 15, "Almacén norte", "2 días"));
		when(exchangeRateClient.getLatestRate()).thenReturn(new ExchangeRateResponse(
				BigDecimal.ONE, "USD", "2026-10-29", Map.of("MXN", new BigDecimal("17.00"))));
	}
}
