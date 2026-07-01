package com.example.productservice.service;

import com.example.productservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRedisServiceImpl implements ProductRedisService {

    private static final String PRODUCT_KEY = "product:";

    private final RedisTemplate<String, Product> redisTemplate;

    @Override
    public Product getProduct(Long id) {

        String key = PRODUCT_KEY + id;

        Product product = (Product) redisTemplate.opsForValue().get(key);

        if (product != null) {
            log.info("✅ Cache HIT : {}", key);
        } else {
            log.info("❌ Cache MISS : {}", key);
        }

        return product;
    }

    @Override
    public void saveProduct(Product product) {

        String key = PRODUCT_KEY + product.getId();

        redisTemplate.opsForValue().set(
                key,
                product,
                Duration.ofMinutes(10));

        log.info("Stored Product {} into Redis", product.getId());

    }

    @Override
    public void deleteProduct(Long id) {

        String key = PRODUCT_KEY + id;

        redisTemplate.delete(key);

        log.info("Deleted Product {} from Redis", id);

    }

}