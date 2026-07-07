package com.example.productservice.service;

import com.example.productservice.constant.RedisStreamConstants;
import com.example.productservice.dto.PendingMessageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.PendingMessagesSummary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamMonitoringServiceImpl
        implements StreamMonitoringService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PendingMessageInfo getPendingMessages() {

        PendingMessagesSummary summary =
                redisTemplate.opsForStream()
                        .pending(
                                RedisStreamConstants.PRODUCT_STREAM,
                                RedisStreamConstants.PRODUCT_GROUP
                        );

        if (summary == null) {

            return new PendingMessageInfo(
                    0L,
                    RedisStreamConstants.PRODUCT_GROUP
            );

        }

        return new PendingMessageInfo(
                summary.getTotalPendingMessages(),
                RedisStreamConstants.PRODUCT_GROUP
        );

    }

}