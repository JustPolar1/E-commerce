package com.store.digital.client.exchange;

import com.store.digital.config.ServiceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExchangeRateClient {

	private final RestClient restClient;
	private final ServiceProperties.Exchange properties;

	public ExchangeRateClient(
			@Qualifier("exchangeRestClient") RestClient restClient,
			ServiceProperties properties) {
		this.restClient = restClient;
		this.properties = properties.exchange();
	}

	public ExchangeRateResponse getLatestRate() {
		return restClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/latest")
						.queryParam("from", properties.baseCurrency())
						.queryParam("to", properties.targetCurrency())
						.build())
				.retrieve()
				.body(ExchangeRateResponse.class);
	}
}
