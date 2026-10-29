package com.store.digital.client.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** Internal product catalog response expected by the gateway. */
public record ProductServiceResponse(
		@JsonProperty("product_id") String productId,
		String title,
		String description,
		String category,
		@JsonProperty("price_usd") BigDecimal priceUsd) {
}
