package com.example.productservice.service;

import com.example.productservice.constant.RedisStreamConstants;
import com.example.productservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductStreamPublisherImpl implements ProductStreamPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publishProductCreated(Product product) {

        StreamOperations<String, Object, Object> stream =
                redisTemplate.opsForStream();

        Map<String, Object> message = Map.of(
                "event", RedisStreamConstants.PRODUCT_CREATED,
                "productId", product.getId(),
                "name", product.getName(),
                "price", product.getPrice()
        );

        RecordId recordId = stream.add(
                MapRecord.create(
                        RedisStreamConstants.PRODUCT_STREAM,
                        message
                )
        );

        log.info("========================================");
        log.info("Published Product Stream Event");
        log.info("Record ID : {}", recordId.getValue());
        log.info("Stream    : {}", RedisStreamConstants.PRODUCT_STREAM);
        log.info("Event     : {}", RedisStreamConstants.PRODUCT_CREATED);
        log.info("Product   : {}", product.getId());
        log.info("========================================");
    }
}