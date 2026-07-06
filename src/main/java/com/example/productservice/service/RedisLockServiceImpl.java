package com.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockServiceImpl implements RedisLockService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean acquireLock(String lockKey) {

        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                "LOCKED",
                Duration.ofSeconds(30)
        );

        if (Boolean.TRUE.equals(success)) {

            log.info("Lock Acquired : {}", lockKey);

            return true;
        }

        log.info("Lock Already Exists : {}", lockKey);

        return false;
    }

    @Override
    public void releaseLock(String lockKey) {

        redisTemplate.delete(lockKey);

        log.info("Lock Released : {}", lockKey);

    }
}