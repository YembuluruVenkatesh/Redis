package com.example.productservice.service;

public interface RedisLockService {

    boolean acquireLock(String lockKey);

    void releaseLock(String lockKey);

}