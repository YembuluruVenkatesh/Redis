package com.example.productservice.service;

import com.example.productservice.constant.RateLimitConstants;
import com.example.productservice.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimiterServiceImpl implements RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void validateRequest(String clientId) {

        String key =
                RateLimitConstants.RATE_LIMIT_PREFIX + clientId;

        Long count =
                redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {

            redisTemplate.expire(
                    key,
                    RateLimitConstants.WINDOW_SECONDS,
                    TimeUnit.SECONDS);

        }

        Long ttl =
                redisTemplate.getExpire(key, TimeUnit.SECONDS);

        log.info("========================================");
        log.info("Redis Rate Limiter");
        log.info("Client   : {}", clientId);
        log.info("Requests : {}", count);
        log.info("TTL      : {} sec", ttl);

        if (count != null &&
                count > RateLimitConstants.MAX_REQUESTS) {

            log.warn("Status   : BLOCKED");
            log.info("========================================");

            throw new RateLimitExceededException(
                    "Too Many Requests. Try again later.");

        }

        log.info("Status   : ALLOWED");
        log.info("========================================");

    }

}