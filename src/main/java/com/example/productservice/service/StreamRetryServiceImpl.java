package com.example.productservice.service;

import com.example.productservice.constant.RedisStreamConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamRetryServiceImpl implements StreamRetryService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void retryPendingMessages() {

        StreamOperations<String, Object, Object> stream =
                redisTemplate.opsForStream();

        List<MapRecord<String, Object, Object>> records =
                stream.read(
                        Consumer.from(
                                RedisStreamConstants.PRODUCT_GROUP,
                                RedisStreamConstants.CONSUMER_NAME
                        ),
                        StreamReadOptions.empty().count(10),
                        StreamOffset.create(
                                RedisStreamConstants.PRODUCT_STREAM,
                                ReadOffset.from("0")
                        )
                );

        if (records == null || records.isEmpty()) {

            log.info("No Pending Messages Found.");

            return;

        }

        for (MapRecord<String, Object, Object> record : records) {

            log.info("====================================");
            log.info("Retrying Message");
            log.info("Record ID : {}", record.getId().getValue());
            log.info("Message   : {}", record.getValue());

            // Simulate processing

            Long ack =
                    stream.acknowledge(
                            RedisStreamConstants.PRODUCT_GROUP,
                            record
                    );

            log.info("ACK Result : {}", ack);
            log.info("====================================");
        }

    }

}