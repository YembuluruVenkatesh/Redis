package com.example.productservice.service;

import com.example.productservice.entity.Product;

public interface ProductStreamPublisher {

    void publishProductCreated(Product product);

}