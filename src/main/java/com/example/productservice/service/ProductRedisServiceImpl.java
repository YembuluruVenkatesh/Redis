package com.example.productservice.service;

import com.example.productservice.constant.RedisConstants;
import com.example.productservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRedisServiceImpl implements ProductRedisService {

    private final RedisTemplate<String, Product> redisTemplate;

    @Override
    public Product getProduct(Long id) {

        String key = RedisConstants.PRODUCT_KEY_PREFIX + id;

        Product product = redisTemplate.opsForValue().get(key);

        if (product != null) {

            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

            log.info("✅ Cache HIT : {}", key);
            log.info("Remaining TTL : {} seconds", ttl);

        } else {

            log.info("❌ Cache MISS : {}", key);

        }

        return product;
    }

    @Override
    public void saveProduct(Product product) {

        String key = RedisConstants.PRODUCT_KEY_PREFIX + product.getId();

        redisTemplate.opsForValue().set(
                key,
                product,
                RedisConstants.PRODUCT_CACHE_TTL);

        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        log.info("Stored Product {} into Redis", product.getId());
        log.info("TTL : {} seconds", ttl);
    }

    @Override
    public void deleteProduct(Long id) {

        String key = RedisConstants.PRODUCT_KEY_PREFIX + id;

        redisTemplate.delete(key);

        log.info("Deleted Product {} from Redis", id);

    }

    @Override
    public Long getTTL(Long id) {

        return redisTemplate.getExpire(
                RedisConstants.PRODUCT_KEY_PREFIX + id,
                TimeUnit.SECONDS);

    }

    @Override
    public boolean exists(Long id) {

        Boolean exists = redisTemplate.hasKey(
                RedisConstants.PRODUCT_KEY_PREFIX + id);

        return Boolean.TRUE.equals(exists);
    }

    @Override
    public void deleteAllProducts() {

        Set<String> keys = redisTemplate.keys("product:*");

        if (keys != null && !keys.isEmpty()) {

            redisTemplate.delete(keys);

            log.info("Deleted {} product cache entries", keys.size());

        } else {

            log.info("No product cache entries found.");

        }

    }

    @Override
    public void clearCache() {

        Objects.requireNonNull(
                        redisTemplate.getConnectionFactory())
                .getConnection()
                .serverCommands()
                .flushAll();

        log.info("Entire Redis cache cleared.");

    }
}