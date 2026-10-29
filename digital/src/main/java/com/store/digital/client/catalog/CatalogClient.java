package com.store.digital.client.catalog;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CatalogClient {

	private final RestClient restClient;

	public CatalogClient(@Qualifier("catalogRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public ProductServiceResponse getProduct(String productId) {
		return restClient.get()
				.uri("/microservices/products/{id}", productId)
				.retrieve()
				.body(ProductServiceResponse.class);
	}
}
