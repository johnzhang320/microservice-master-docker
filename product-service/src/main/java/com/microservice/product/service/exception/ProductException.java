package com.microservice.product.service.exception;

public class ProductException extends RuntimeException{
    public ProductException(String message) {
        super(message);
    }
}
