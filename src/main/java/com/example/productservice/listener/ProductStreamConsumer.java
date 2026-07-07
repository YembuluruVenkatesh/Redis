package com.example.productservice.listener;

import com.example.productservice.constant.RedisStreamConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
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

        createConsumerGroup();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {

            StreamOperations<String, Object, Object> stream =
                    redisTemplate.opsForStream();

            while (true) {

                try {

                    List<MapRecord<String, Object, Object>> records =
                            stream.read(
                                    Consumer.from(
                                            RedisStreamConstants.CONSUMER_GROUP,
                                            RedisStreamConstants.CONSUMER_NAME
                                    ),
                                    StreamReadOptions.empty()
                                            .block(Duration.ofSeconds(2)),
                                    StreamOffset.create(
                                            RedisStreamConstants.PRODUCT_STREAM,
                                            ReadOffset.lastConsumed()
                                    )
                            );
                    // ReadOffset.from("0-0") - --  want to replay everything from the beginning
                    // ReadOffset.latest() - -- Read only the newest messages.
                    // ReadOffset.lastConsumed() -- Read only the messages that this Consumer Group has NOT processed yet.
                    log.info("Record : {}", records);
                    if (records != null && !records.isEmpty()) {

                        for (MapRecord<String, Object, Object> record : records) {

                            log.info("-------------------------------------");
                            log.info("Received Stream Message");
                            log.info("Record ID : {}", record.getId());
                            log.info("Message   : {}", record.getValue());
                            log.info("-------------------------------------");

                            // ACK the message
                            stream.acknowledge(
                                    RedisStreamConstants.CONSUMER_GROUP,
                                    record
                            );
                        }
                    }

                } catch (Exception ex) {

                    log.error("Stream Consumer Error", ex);

                }

            }

        });

    }

    /**
     * Creates Consumer Group only once.
     */
    private void createConsumerGroup() {

        try {

            redisTemplate.opsForStream().createGroup(
                    RedisStreamConstants.PRODUCT_STREAM,
                    ReadOffset.from("0-0"),
                    RedisStreamConstants.CONSUMER_GROUP
            );

            log.info("Consumer Group Created.");

        } catch (RedisSystemException ex) {

            log.info("Consumer Group already exists.");

        }

    }

}