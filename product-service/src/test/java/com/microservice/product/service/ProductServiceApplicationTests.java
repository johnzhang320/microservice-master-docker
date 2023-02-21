package com.microservice.product.service;

import com.microservice.product.service.dto.ProductRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.18");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	/**
	 * spring:
	 *   data:
	 *     mongodb:
	 *       host: localhost
	 *       port: 27017
	 *       database: mygrocerylist
	 *       username: groceryUser
	 *       password: xyz123
	 *
	 *       full uri format
	 *       mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[defaultauthdb][?options]]
	 *       spring.data.mongodb.uri = mongodb://groceryUser:xyz123@localhost:27017/mygrocerylist
	 * @param dynamicPropertyRegistry
	 */
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		List list = new ArrayList<>();
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateProducts() throws Exception {
		ProductRequestDto requestDto = getProductRequestDto();
		String productRequestDtoString = objectMapper.writeValueAsString(requestDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestDtoString))
				.andExpect(status().isCreated());
	}
	private ProductRequestDto getProductRequestDto() {
		return ProductRequestDto.builder()
				.name("Blood Oxygen Meter")
				.description("Measure Density of Oxygen in blood")
				.price(new BigDecimal(2500))
				.build();

	}

}
