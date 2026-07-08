package com.example.productservice.service;

import com.example.productservice.dto.RedisInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class RedisMonitoringServiceImpl
        implements RedisMonitoringService {

    private final RedisConnectionFactory connectionFactory;

    @Override
    public RedisInfo getInfo() {

        try (RedisConnection connection =
                     connectionFactory.getConnection()) {

            Properties info = connection.serverCommands().info();

            Long totalKeys = connection.dbSize();
            String hits = info.getProperty("keyspace_hits");
            String misses = info.getProperty("keyspace_misses");

            double ratio = 0;

            if (hits != null && misses != null) {

                long h = Long.parseLong(hits);
                long m = Long.parseLong(misses);

                if (h + m > 0) {
                    ratio = (double) h * 100 / (h + m);
                }
            }
            return RedisInfo.builder()

                    .redisVersion(info.getProperty("redis_version"))

                    .uptime(info.getProperty("uptime_in_seconds"))

                    .connectedClients(info.getProperty("connected_clients"))

                    .usedMemory(info.getProperty("used_memory_human"))

                    .expiredKeys(info.getProperty("expired_keys"))

                    .evictedKeys(info.getProperty("evicted_keys"))

                    .totalKeys(String.valueOf(totalKeys))
                    .redisMode(info.getProperty("redis_mode"))
                    .role(info.getProperty("role"))
                    .totalConnections(info.getProperty("total_connections_received"))
                    .totalCommandsProcessed(info.getProperty("total_commands_processed"))
                    .instantaneousOpsPerSec(info.getProperty("instantaneous_ops_per_sec"))
                    .keyspaceHits(hits)
                    .keyspaceMisses(misses)
                    .hitRatio(String.format("%.2f %%", ratio))
                    .build();

        }

    }

}