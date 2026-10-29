package com.store.digital.client.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Internal inventory response expected by the gateway. */
public record InventoryServiceResponse(
		@JsonProperty("product_id") String productId,
		@JsonProperty("stock_status") String stockStatus,
		@JsonProperty("available_units") int availableUnits,
		@JsonProperty("warehouse_location") String warehouseLocation,
		@JsonProperty("estimated_delivery") String estimatedDelivery) {
}
