package com.store.digital.controller;

import com.store.digital.dto.response.ProductSummaryResponse;
import com.store.digital.service.ProductSummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductSummaryController {

	private final ProductSummaryService productSummaryService;

	public ProductSummaryController(ProductSummaryService productSummaryService) {
		this.productSummaryService = productSummaryService;
	}

	@GetMapping("/{id}/summary")
	public ProductSummaryResponse getProductSummary(@PathVariable("id") String productId) {
		return productSummaryService.getProductSummary(productId);
	}
}
