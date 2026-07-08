package com.example.productservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedisInfo {

    private String redisVersion;

    private String uptime;

    private String connectedClients;

    private String usedMemory;

    private String totalKeys;

    private String expiredKeys;

    private String evictedKeys;

    private String redisMode;
    private String role;
    private String totalConnections;
    private String totalCommandsProcessed;
    private String instantaneousOpsPerSec;
    private String keyspaceHits;
    private String keyspaceMisses;
    private String hitRatio;

}