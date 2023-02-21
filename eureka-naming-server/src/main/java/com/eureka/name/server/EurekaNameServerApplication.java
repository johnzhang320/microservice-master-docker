package com.eureka.name.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *  youtube link
 *  https://www.google.com/search?q=applicant-services+eureka+feign+youtube&sxsrf=AJOqlzWsNQPZNQCwW81DPWfZ2DCs6OeNmQ%3A1675629753413&ei=uRTgY5brGP6z0PEP7YuR6Ak&ved=0ahUKEwjWq-ihn__8AhX-GTQIHe1FBJ0Q4dUDCBA&uact=5&oq=applicant-services+eureka+feign+youtube&gs_lcp=Cgxnd3Mtd2l6LXNlcnAQAzIICCEQoAEQwwQ6CggAEEcQ1gQQsAM6BQgAEKIESgQIQRgASgQIRhgAUIEVWKwbYN8faANwAXgAgAF5iAHSAZIBAzEuMZgBAKABAcgBB8ABAQ&sclient=gws-wiz-serp#fpstate=ive&vld=cid:31a7c65c,vid:l489l1gTvEI
 */

/**
 *  Integrate Eureka Server , Eureka Client, Resilient4J , OpenFeign Proxy services call each other
 *  and api way
 *  http://localhost:8761
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaNameServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaNameServerApplication.class, args);
	}

}
