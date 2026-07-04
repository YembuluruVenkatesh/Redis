package com.example.productservice.service;

import com.example.productservice.entity.Product;

public interface ProductHashService {

    void save(Product product);

    Product get(Long id);

    void updatePrice(Long id, Double price);

    void delete(Long id);

}