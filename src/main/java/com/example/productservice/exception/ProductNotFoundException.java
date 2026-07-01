package com.example.productservice.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Requested product with ID " + id + " is not available.");
    }

}