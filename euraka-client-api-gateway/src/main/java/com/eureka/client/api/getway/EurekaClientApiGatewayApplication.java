package com.eureka.client.api.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaClientApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApiGatewayApplication.class, args);
	}

}
