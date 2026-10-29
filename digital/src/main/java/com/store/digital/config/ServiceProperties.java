package com.store.digital.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services")
public record ServiceProperties(
		Catalog catalog,
		Inventory inventory,
		Exchange exchange) {

	public record Catalog(String baseUrl) {
	}

	public record Inventory(String baseUrl) {
	}

	public record Exchange(String baseUrl, String baseCurrency, String targetCurrency) {
	}
}
