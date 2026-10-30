package com.store.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record ProductResponse(
		@JsonProperty("product_id") String productId,
		String title,
		String description,
		String category,
		@JsonProperty("price_usd") BigDecimal priceUsd) {
}
