package com.example.productservice.service;

import java.util.List;

public interface ProductRecentService {
    void addRecentProduct(Long productId);

    List<Object> getRecentProducts();

    void clearRecentProducts();
}
