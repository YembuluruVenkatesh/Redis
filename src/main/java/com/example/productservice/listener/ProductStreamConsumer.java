package com.example.productservice.listener;

import com.example.productservice.constant.RedisStreamConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductStreamConsumer {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void startConsumer() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {

            StreamOperations<String, Object, Object> stream =
                    redisTemplate.opsForStream();

            while (true) {

                try {

                    List<MapRecord<String, Object, Object>> records =
                            stream.read(
                                    Consumer.from(
                                            RedisStreamConstants.PRODUCT_GROUP,
                                            RedisStreamConstants.CONSUMER_NAME
                                    ),
                                    StreamReadOptions.empty()
                                            .block(Duration.ofSeconds(2))
                                            .count(10),
                                    StreamOffset.create(
                                            RedisStreamConstants.PRODUCT_STREAM,
                                            ReadOffset.lastConsumed()
                                    )
                            );

                    if (records == null || records.isEmpty()) {
                        continue;
                    }

                    for (MapRecord<String, Object, Object> record : records) {

                        log.info("======================================");
                        log.info("Received Stream Message");
                        log.info("Record ID : {}", record.getId().getValue());
                        log.info("Message   : {}", record.getValue());

                        /*
                         * Simulate business logic
                         */

                        Thread.sleep(500);

                        /*
                         * ACKNOWLEDGE
                         */

                        Long acknowledged =
                                stream.acknowledge(
                                        RedisStreamConstants.PRODUCT_GROUP,
                                        record
                                );

                        log.info("ACK Success : {}", acknowledged);

                        log.info("======================================");
                    }

                } catch (Exception ex) {

                    log.error("Consumer Error", ex);

                }

            }

        });

    }

}