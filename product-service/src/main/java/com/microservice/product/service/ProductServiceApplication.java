package com.microservice.product.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProductServiceApplication {
	/**
	 * http://localhost:8091/products/findAll
	 * http://localhost:8091/products/findProductById/63e2b10db5176c6409fcb611
	 * http://localhost:8091/products/findProductById/63e2afdbb5176c6409fcb610
	 * http://localhost:8091/products/findProductById/63e2b14db5176c6409fcb612
	 * http://localhost:8091/products/create
	 * {
	 *
	 *                 "productId": "63e2afdbb5176c6409fcb610",
	 *                 "productName": "HP 2000",
	 *                 "quantity": 200,
	 *                 "skuCode" : "HP 2000"
	 *
	 * }
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
