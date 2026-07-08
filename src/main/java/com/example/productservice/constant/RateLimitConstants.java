package com.example.productservice.constant;

public final class RateLimitConstants {

    private RateLimitConstants() {
    }

    public static final String RATE_LIMIT_PREFIX = "rate-limit:";

    public static final long MAX_REQUESTS = 5;

    public static final long WINDOW_SECONDS = 60;

}