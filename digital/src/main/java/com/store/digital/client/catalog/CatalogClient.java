package com.store.digital.client.catalog;

import com.store.digital.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CatalogClient {

	private final RestClient restClient;

	public CatalogClient(@Qualifier("catalogRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public ProductServiceResponse getProduct(String productId) {
		try {
			return restClient.get()
					.uri("/microservices/products/{id}", productId)
					.retrieve()
					.body(ProductServiceResponse.class);
		} catch (HttpClientErrorException.NotFound exception) {
			throw new ProductNotFoundException(productId, exception);
		}
	}
}
