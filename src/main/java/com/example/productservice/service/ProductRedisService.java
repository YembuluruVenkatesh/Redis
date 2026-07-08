package com.example.productservice.service;

import com.example.productservice.entity.Product;

import java.util.List;

public interface ProductRedisService {

    void save(Product product);

    Product get(Long id);

    boolean exists(Long id);

    Long getTTL(Long id);

    void delete(Long id);

    void deleteAllProducts();

    void clearCache();

    List<Product> getAllProducts();

}