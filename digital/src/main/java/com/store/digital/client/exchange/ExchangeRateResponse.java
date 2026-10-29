package com.store.digital.client.exchange;

import java.math.BigDecimal;
import java.util.Map;

/** Frankfurter response containing the requested currency rates. */
public record ExchangeRateResponse(
		BigDecimal amount,
		String base,
		String date,
		Map<String, BigDecimal> rates) {

	public BigDecimal rateFor(String currency) {
		return rates == null ? null : rates.get(currency);
	}
}
