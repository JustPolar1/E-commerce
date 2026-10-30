package com.store.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class InventoryController {
	private static final InventoryResponse SAMPLE_INVENTORY = new InventoryResponse(
			"P-101", "IN_STOCK", 15, "Almacén norte", "2 días");

	@GetMapping("/microservices/inventory/{productId}")
	public InventoryResponse getInventory(@PathVariable String productId) {
		if (!SAMPLE_INVENTORY.productId().equals(productId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
		}
		return SAMPLE_INVENTORY;
	}
}
