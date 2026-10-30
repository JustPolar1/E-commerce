package com.store.inventory;

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
class InventoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void returnsInventoryDetails() throws Exception {
		mockMvc.perform(get("/microservices/inventory/P-101"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.product_id").value("P-101"))
				.andExpect(jsonPath("$.stock_status").value("IN_STOCK"))
				.andExpect(jsonPath("$.available_units").value(15))
				.andExpect(jsonPath("$.warehouse_location").value("Almacén norte"));
	}

	@Test
	void returnsNotFoundForUnknownProduct() throws Exception {
		mockMvc.perform(get("/microservices/inventory/P-404"))
				.andExpect(status().isNotFound());
	}
}
