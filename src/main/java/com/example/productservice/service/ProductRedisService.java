package com.example.productservice.service;

import com.example.productservice.entity.Product;

public interface ProductRedisService {

    Product getProduct(Long id);

    void saveProduct(Product product);

    void deleteProduct(Long id);

    Long getTTL(Long id);

    boolean exists(Long id);

}