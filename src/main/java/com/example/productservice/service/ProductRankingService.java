package com.example.productservice.service;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface ProductRankingService {

    void incrementView(Long productId);

    //Set<Object> topProducts(int count);

    Set<ZSetOperations.TypedTuple<Object>> topProducts(int count);

    Double getScore(Long productId);

    Long getRank(Long productId);

    void removeProduct(Long productId);
}