package com.microservice.order.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
	/**
	 * http://localhost:8081/orders/placeOrder
	 * http://localhost:8081/orders/findAll
	 * set Docker by confluent
	 * https://developer.confluent.io/quickstart/kafka-docker
	 * because I still use Intellij community version, which can not auto-complete
	 * I prefer use configuration file to configure the producer and consumer
	 * kafkaTemplate configuration
	 * https://spring.io/projects/spring-kafka#overview
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
