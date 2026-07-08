package com.example.productservice.service;

public interface RateLimiterService {

    void validateRequest(String clientId);

}