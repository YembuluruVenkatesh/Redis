package com.example.productservice.constant;

import java.time.Duration;

public final class RedisSessionConstants {

    private RedisSessionConstants() {
    }

    public static final String SESSION_PREFIX = "session:";

    public static final Duration SESSION_TTL = Duration.ofMinutes(30);

}