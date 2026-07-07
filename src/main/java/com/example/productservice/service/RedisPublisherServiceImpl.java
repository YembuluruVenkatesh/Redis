package com.example.productservice.service;

import com.example.productservice.constant.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisherServiceImpl
        implements RedisPublisherService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(String message) {

        redisTemplate.convertAndSend(
                RedisConstants.PRODUCT_CHANNEL,
                message);

        log.info("Published Event : {}", message);

    }
}