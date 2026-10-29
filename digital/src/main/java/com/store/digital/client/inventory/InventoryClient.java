package com.store.digital.client.inventory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InventoryClient {

	private final RestClient restClient;

	public InventoryClient(@Qualifier("inventoryRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public InventoryServiceResponse getInventory(String productId) {
		return restClient.get()
				.uri("/microservices/inventory/{productId}", productId)
				.retrieve()
				.body(InventoryServiceResponse.class);
	}
}
