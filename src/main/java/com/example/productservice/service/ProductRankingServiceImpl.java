package com.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingServiceImpl
        implements ProductRankingService {

    private static final String KEY = "product:views";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void incrementView(Long productId) {

        redisTemplate.opsForZSet()
                .incrementScore(KEY, productId, 1);

        log.info("Incremented Product {} View Count", productId);
    }

    /*@Override
    public Set<Object> topProducts(int count) {

        return redisTemplate.opsForZSet()
                .reverseRange(KEY, 0, count - 1);
    }*/

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> topProducts(int count) {

        return redisTemplate.opsForZSet()
                .reverseRangeWithScores(KEY, 0, count - 1);
    }

    @Override
    public Double getScore(Long productId) {

        return redisTemplate.opsForZSet()
                .score(KEY, productId);
    }

    @Override
    public Long getRank(Long productId) {

        return redisTemplate.opsForZSet()
                .reverseRank(KEY, productId);
    }

    @Override
    public void removeProduct(Long productId) {

        redisTemplate.opsForZSet()
                .remove(KEY, productId);

        log.info("Removed Product {} from Ranking", productId);
    }
}