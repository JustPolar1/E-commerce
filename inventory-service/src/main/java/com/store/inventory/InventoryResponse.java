package com.store.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InventoryResponse(
		@JsonProperty("product_id") String productId,
		@JsonProperty("stock_status") String stockStatus,
		@JsonProperty("available_units") int availableUnits,
		@JsonProperty("warehouse_location") String warehouseLocation,
		@JsonProperty("estimated_delivery") String estimatedDelivery) {
}
