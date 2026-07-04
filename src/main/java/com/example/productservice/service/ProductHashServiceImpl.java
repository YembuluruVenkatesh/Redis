package com.example.productservice.service;

import com.example.productservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductHashServiceImpl implements ProductHashService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "product:hash:";

    @Override
    public void save(Product product) {

        HashOperations<String, String, Object> hash =
                redisTemplate.opsForHash();

        String key = KEY_PREFIX + product.getId();

        hash.put(key, "id", product.getId());
        hash.put(key, "name", product.getName());
        hash.put(key, "price", product.getPrice());

        log.info("Stored Product {} as Redis HASH", product.getId());
    }

    @Override
    public Product get(Long id) {

        HashOperations<String, String, Object> hash =
                redisTemplate.opsForHash();

        String key = KEY_PREFIX + id;

        if (!redisTemplate.hasKey(key)) {

            log.info("HASH Cache MISS : {}", key);
            return null;
        }

        Product product = new Product();

        product.setId(
                Long.valueOf(hash.get(key, "id").toString()));

        product.setName(
                hash.get(key, "name").toString());

        product.setPrice(
                Double.valueOf(hash.get(key, "price").toString()));

        log.info("HASH Cache HIT : {}", key);

        return product;
    }

    @Override
    public void updatePrice(Long id, Double price) {

        String key = KEY_PREFIX + id;

        redisTemplate.opsForHash()
                .put(key, "price", price);

        log.info("Updated HASH price for Product {}", id);
    }

    @Override
    public void delete(Long id) {

        redisTemplate.delete(KEY_PREFIX + id);

        log.info("Deleted HASH Product {}", id);
    }
}