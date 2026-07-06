package com.example.productservice.service;

import java.util.Set;

public interface ProductFavoriteService {

    void addFavorite(Long userId, Long productId);

    void removeFavorite(Long userId, Long productId);

    Set<Object> getFavorites(Long userId);

    boolean isFavorite(Long userId, Long productId);

    Long totalFavorites(Long userId);

}