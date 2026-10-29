package com.store.digital.exception;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(String productId) {
		super("Product was not found: " + productId);
	}

	public ProductNotFoundException(String productId, Throwable cause) {
		super("Product was not found: " + productId, cause);
	}
}
