package com.store.digital.config;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ServiceClientsConfig {

	@Bean
	ClientHttpRequestFactory downstreamRequestFactory() {
		var requestFactory = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(3))
				.build());
		requestFactory.setReadTimeout(Duration.ofSeconds(5));
		return requestFactory;
	}

	@Bean
	@Qualifier("catalogRestClient")
	RestClient catalogRestClient(ServiceProperties properties, ClientHttpRequestFactory requestFactory) {
		return RestClient.builder()
				.baseUrl(properties.catalog().baseUrl())
				.requestFactory(requestFactory)
				.build();
	}

	@Bean
	@Qualifier("inventoryRestClient")
	RestClient inventoryRestClient(ServiceProperties properties, ClientHttpRequestFactory requestFactory) {
		return RestClient.builder()
				.baseUrl(properties.inventory().baseUrl())
				.requestFactory(requestFactory)
				.build();
	}

	@Bean
	@Qualifier("exchangeRestClient")
	RestClient exchangeRestClient(ServiceProperties properties, ClientHttpRequestFactory requestFactory) {
		return RestClient.builder()
				.baseUrl(properties.exchange().baseUrl())
				.requestFactory(requestFactory)
				.build();
	}
}
