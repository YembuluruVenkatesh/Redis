package com.example.productservice.service;

import com.example.productservice.constant.RedisConstants;
import com.example.productservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRedisServiceImpl implements ProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(Product product) {

        String key = RedisConstants.PRODUCT_CACHE_KEY + product.getId();

        redisTemplate.opsForValue().set(
                key,
                product,
                RedisConstants.CACHE_TTL,
                TimeUnit.SECONDS);

        log.info("Stored Product {} into Redis", product.getId());

        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        log.info("TTL : {} seconds", ttl);
    }

    @Override
    public Product get(Long id) {

        return (Product) redisTemplate.opsForValue()
                .get(RedisConstants.PRODUCT_CACHE_KEY + id);
    }

    @Override
    public boolean exists(Long id) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(
                        RedisConstants.PRODUCT_CACHE_KEY + id));
    }

    @Override
    public Long getTTL(Long id) {

        return redisTemplate.getExpire(
                RedisConstants.PRODUCT_CACHE_KEY + id,
                TimeUnit.SECONDS);
    }

    @Override
    public void delete(Long id) {

        redisTemplate.delete(
                RedisConstants.PRODUCT_CACHE_KEY + id);

        log.info("Deleted Product {} from Redis", id);
    }

    @Override
    public void deleteAllProducts() {

        Set<String> keys = redisTemplate.keys(
                RedisConstants.PRODUCT_CACHE_KEY + "*");

        if (keys != null && !keys.isEmpty()) {

            redisTemplate.delete(keys);

            log.info("Deleted Product Cache");

        }
    }

    @Override
    public void clearCache() {

        Set<String> keys = redisTemplate.keys("*");

        if (keys != null && !keys.isEmpty()) {

            redisTemplate.delete(keys);

            log.info("Entire Redis Cache Cleared");

        }
    }

    @Override
    public List<Product> getAllProducts() {

        Set<String> keys = redisTemplate.keys(
                RedisConstants.PRODUCT_CACHE_KEY + "*");

        List<Product> products = new ArrayList<>();

        if (keys != null) {

            for (String key : keys) {

                Product product =
                        (Product) redisTemplate.opsForValue().get(key);

                if (product != null) {

                    products.add(product);

                }
            }
        }

        return products;
    }
}