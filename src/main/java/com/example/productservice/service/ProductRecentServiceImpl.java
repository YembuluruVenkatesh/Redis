package com.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRecentServiceImpl
        implements ProductRecentService {

    private static final String KEY = "recent:products";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addRecentProduct(Long productId) {

        redisTemplate.opsForList().remove(KEY, 0, productId);

        redisTemplate.opsForList().leftPush(KEY, productId);

        redisTemplate.opsForList().trim(KEY, 0, 9);

        log.info("Updated Recent Product List with Product {}", productId);
    }

    @Override
    public List<Object> getRecentProducts() {

        List<Object> products =
                redisTemplate.opsForList()
                        .range(KEY, 0, -1);

        log.info("Fetched Recent Product List");

        return products;
    }

    @Override
    public void clearRecentProducts() {

        redisTemplate.delete(KEY);

        log.info("Recent Product List Cleared");
    }
}