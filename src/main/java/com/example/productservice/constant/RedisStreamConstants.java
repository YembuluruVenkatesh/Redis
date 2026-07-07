package com.example.productservice.constant;

public final class RedisStreamConstants {

    private RedisStreamConstants() {
    }

    // Stream
    public static final String PRODUCT_STREAM = "product-stream";

    // Consumer Group
    public static final String PRODUCT_GROUP = "product-group";
    public static final String CONSUMER_NAME = "consumer-1";

    // Events
    public static final String PRODUCT_CREATED = "PRODUCT_CREATED";

}