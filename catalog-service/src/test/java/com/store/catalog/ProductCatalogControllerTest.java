package com.store.catalog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductCatalogControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void returnsProductDetails() throws Exception {
		mockMvc.perform(get("/microservices/products/P-101"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.product_id").value("P-101"))
				.andExpect(jsonPath("$.title").value("Laptop Gamer XYZ"))
				.andExpect(jsonPath("$.price_usd").value(1200.00));
	}

	@Test
	void returnsNotFoundForUnknownProduct() throws Exception {
		mockMvc.perform(get("/microservices/products/P-404"))
				.andExpect(status().isNotFound());
	}
}
