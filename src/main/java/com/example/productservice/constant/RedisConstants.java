package com.example.productservice.constant;

import java.time.Duration;

public final class RedisConstants {

    private RedisConstants() {
    }

    // Single Product Cache
    public static final String PRODUCT_KEY_PREFIX = "product:";
    public static final Duration PRODUCT_CACHE_TTL = Duration.ofMinutes(5);

    // Product List Cache
    public static final String PRODUCT_LIST_KEY = "products:all";
    public static final Duration PRODUCT_LIST_TTL = Duration.ofMinutes(2);

}