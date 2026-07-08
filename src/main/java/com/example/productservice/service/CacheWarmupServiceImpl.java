package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheWarmupServiceImpl implements CacheWarmupService, ApplicationRunner {

    private final ProductRepository repository;
    private final ProductRedisService redisService;

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {

        warmup();

    }

    @Override
    public void warmup() {

        log.info("==========================================");
        log.info("Starting Redis Cache Warm-up...");
        log.info("==========================================");

        List<Product> products = repository.findAll();

        for (Product product : products) {

            redisService.save(product);

            log.info("Cached Product : {} - {}",
                    product.getId(),
                    product.getName());

        }

        log.info("==========================================");
        log.info("Cache Warm-up Completed");
        log.info("Products Cached : {}", products.size());
        log.info("==========================================");

    }

}