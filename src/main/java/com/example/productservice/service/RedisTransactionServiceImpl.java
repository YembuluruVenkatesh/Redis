package com.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTransactionServiceImpl
        implements RedisTransactionService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void executeTransaction() {

        redisTemplate.execute(new SessionCallback<>() {

            @Override
            public Object execute(org.springframework.data.redis.core.RedisOperations operations) {

                operations.multi();

                operations.opsForValue().set(
                        "transaction:name",
                        "Redis Learning");

                operations.opsForValue().set(
                        "transaction:version",
                        "1.0");

                operations.opsForValue().increment(
                        "transaction:counter");

                operations.exec();

                return null;
            }

        });

        log.info("Redis Transaction Executed Successfully");
    }
}