package com.microservice.order.service.exception;

public class OrderException extends RuntimeException{
    public OrderException(String message) {
        super(message);
    }
}
