package com.store.digital.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record ProductSummaryResponse(
		@JsonProperty("product_id") String productId,
		String title,
		String description,
		String category,
		@JsonProperty("price_usd") BigDecimal priceUsd,
		@JsonProperty("price_mxn") BigDecimal priceMxn,
		@JsonProperty("stock_status") String stockStatus,
		@JsonProperty("available_units") int availableUnits,
		@JsonProperty("warehouse_location") String warehouseLocation,
		@JsonProperty("estimated_delivery") String estimatedDelivery,
		@JsonProperty("currency_conversion_source") String currencyConversionSource,
		String status) {
}
