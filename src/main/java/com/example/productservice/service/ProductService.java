package com.example.productservice.service;

import com.example.productservice.entity.Product;

import java.util.List;

public interface ProductService {

    Product save(Product product);

    Product getProduct(Long id);

    List<Product> getAllProducts();

    Product update(Long id, Product product);

    void delete(Long id);

    List<Product> searchProducts(String keyword);
}