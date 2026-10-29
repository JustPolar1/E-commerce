package com.store.digital.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ServiceClientsConfig {

	@Bean
	@Qualifier("catalogRestClient")
	RestClient catalogRestClient(ServiceProperties properties) {
		return RestClient.builder().baseUrl(properties.catalog().baseUrl()).build();
	}

	@Bean
	@Qualifier("inventoryRestClient")
	RestClient inventoryRestClient(ServiceProperties properties) {
		return RestClient.builder().baseUrl(properties.inventory().baseUrl()).build();
	}

	@Bean
	@Qualifier("exchangeRestClient")
	RestClient exchangeRestClient(ServiceProperties properties) {
		return RestClient.builder().baseUrl(properties.exchange().baseUrl()).build();
	}
}
