package com.eureka.client.api.getway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGetwayConfiguration {
    @Bean
    public RouteLocator getwayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p->p.path("/get")
                        .uri("http://httpbin.org"))
                .route(p->p
                        .path("/products/**")
                        .uri("lb://product-services"))
                .route(p->p
                        .path("/inventorys/**")
                        .uri("lb://inventory-services"))
                .route(p->p
                        .path("/orders/**")
                        .uri("lb://order-services"))
                .build();
    }
}
