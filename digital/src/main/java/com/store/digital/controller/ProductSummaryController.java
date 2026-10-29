package com.store.digital.controller;

import com.store.digital.dto.response.ProductSummaryResponse;
import com.store.digital.service.ProductSummaryService;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/api/v1/products")
public class ProductSummaryController {

	private final ProductSummaryService productSummaryService;

	public ProductSummaryController(ProductSummaryService productSummaryService) {
		this.productSummaryService = productSummaryService;
	}

	@GetMapping("/{id}/summary")
	public ProductSummaryResponse getProductSummary(
			@PathVariable("id") @Pattern(regexp = "[A-Za-z0-9][A-Za-z0-9._-]{0,63}") String productId) {
		return productSummaryService.getProductSummary(productId);
	}
}
