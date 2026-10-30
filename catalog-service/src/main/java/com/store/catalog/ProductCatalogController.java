package com.store.catalog;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProductCatalogController {
	private static final ProductResponse SAMPLE_PRODUCT = new ProductResponse(
			"P-101",
			"Laptop Gamer XYZ",
			"Laptop para videojuegos con pantalla de alta frecuencia.",
			"Computación",
			new BigDecimal("1200.00"));

	@GetMapping("/microservices/products/{id}")
	public ProductResponse getProduct(@PathVariable String id) {
		if (!SAMPLE_PRODUCT.productId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
		}
		return SAMPLE_PRODUCT;
	}
}
