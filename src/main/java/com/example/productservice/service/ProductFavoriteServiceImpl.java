package com.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductFavoriteServiceImpl
        implements ProductFavoriteService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "user:";

    private String key(Long userId) {
        return PREFIX + userId + ":favorites";
    }

    @Override
    public void addFavorite(Long userId, Long productId) {

        redisTemplate.opsForSet()
                .add(key(userId), productId);

        log.info("User {} added Product {} to favorites",
                userId, productId);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {

        redisTemplate.opsForSet()
                .remove(key(userId), productId);

        log.info("User {} removed Product {}",
                userId, productId);
    }

    @Override
    public Set<Object> getFavorites(Long userId) {

        log.info("Fetching favorite products for User {}", userId);

        return redisTemplate.opsForSet()
                .members(key(userId));
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {

        Boolean result = redisTemplate.opsForSet()
                .isMember(key(userId), productId);

        return Boolean.TRUE.equals(result);
    }

    @Override
    public Long totalFavorites(Long userId) {

        return redisTemplate.opsForSet()
                .size(key(userId));
    }
}