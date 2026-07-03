package com.example.productservice.service;

import com.example.productservice.constant.RedisConstants;
import com.example.productservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRedisServiceImpl implements ProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Product getProduct(Long id) {

        String key = RedisConstants.PRODUCT_KEY_PREFIX + id;

        Product product = (Product) redisTemplate.opsForValue().get(key);

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

    @Override
    public List<Product> getAllProducts() {

        Object value = redisTemplate.opsForValue()
                .get(RedisConstants.PRODUCT_LIST_KEY);

        if (value != null) {

            log.info("✅ Cache HIT : {}", RedisConstants.PRODUCT_LIST_KEY);

            return objectMapper.convertValue(
                    value,
                    new TypeReference<List<Product>>() {}
            );
        }

        log.info("❌ Cache MISS : {}", RedisConstants.PRODUCT_LIST_KEY);

        return null;
    }


    @Override
    public void saveAllProducts(List<Product> products) {

        redisTemplate.opsForValue().set(
                RedisConstants.PRODUCT_LIST_KEY,
                products,
                RedisConstants.PRODUCT_LIST_TTL
        );

        log.info("Stored Product List into Redis");
    }

    @Override
    public void deleteAllProductsCache() {

        redisTemplate.delete(RedisConstants.PRODUCT_LIST_KEY);

        log.info("Deleted Product List Cache");
    }
}