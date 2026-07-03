package com.example.productservice.service;

import com.example.productservice.entity.Product;

import java.util.List;

public interface ProductRedisService {

    Product getProduct(Long id);

    void saveProduct(Product product);

    void deleteProduct(Long id);

    Long getTTL(Long id);

    boolean exists(Long id);

    void deleteAllProducts();

    void clearCache();

    List<Product> getAllProducts();

    void saveAllProducts(List<Product> products);

    void deleteAllProductsCache();
}