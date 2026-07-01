package com.example.productservice.constant;

import java.time.Duration;

public final class RedisConstants {

    private RedisConstants() {
    }

    public static final String PRODUCT_KEY_PREFIX = "product:";

    public static final Duration PRODUCT_CACHE_TTL = Duration.ofMinutes(5);

}