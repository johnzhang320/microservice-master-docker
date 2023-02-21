package com.microservice.inventory.exception;

public class InventoryException extends RuntimeException{
    public InventoryException(String message) {
        super(message);
    }
}
