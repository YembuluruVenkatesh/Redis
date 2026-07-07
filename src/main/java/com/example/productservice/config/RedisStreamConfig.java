package com.example.productservice.config;

import com.example.productservice.constant.RedisStreamConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisStreamConfig {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initializeStream() {

        try {

            RedisConnection connection =
                    redisTemplate.getConnectionFactory().getConnection();

            // Create stream if it doesn't exist
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(RedisStreamConstants.PRODUCT_STREAM))) {

                redisTemplate.opsForStream().add(
                        RedisStreamConstants.PRODUCT_STREAM,
                        java.util.Map.of("init", "stream-created")
                );

                log.info("Product Stream Created");
            }

            try {

                connection.streamCommands().xGroupCreate(
                        RedisStreamConstants.PRODUCT_STREAM.getBytes(),
                        RedisStreamConstants.PRODUCT_GROUP,
                        ReadOffset.from("0-0"),
                        true
                );

                log.info("Consumer Group Created");

            } catch (DataAccessException ex) {

                log.info("Consumer Group already exists");

            }

        } catch (Exception ex) {

            log.error("Unable to initialize Stream", ex);

        }

    }

}