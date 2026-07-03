package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductRedisService;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;
    private final ProductRedisService redisService;
    @PostMapping
    public Product save(@RequestBody Product product) {

        log.info("Received request to create product: {}", product);

        Product savedProduct = service.save(product);

        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return savedProduct;
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {

        log.info("Received request to fetch product with ID: {}", id);

        Product product = service.getProduct(id);

        log.info("Returning product with ID: {}", id);

        return product;
    }

    @GetMapping
    public List<Product> getAllProducts() {

        log.info("Received request to fetch all products");

        List<Product> products = service.getAllProducts();

        log.info("Returning {} products", products.size());

        return products;
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id,
                          @RequestBody Product product) {

        log.info("Received request to update product with ID: {}", id);

        Product updatedProduct = service.update(id, product);

        log.info("Product updated successfully with ID: {}", id);

        return updatedProduct;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        log.info("Received request to delete product with ID: {}", id);

        service.delete(id);

        log.info("Product deleted successfully with ID: {}", id);
    }

    @GetMapping("/cache/{id}")
    public String cacheInfo(@PathVariable Long id) {

        boolean exists = redisService.exists(id);

        Long ttl = redisService.getTTL(id);

        return """
            Product Cache Details
            
            Exists : %s
            
            TTL : %d seconds
            """
                .formatted(exists, ttl);
    }

    @DeleteMapping("/cache")
    public String clearProductCache() {

        redisService.deleteAllProducts();

        return "All product cache entries deleted.";

    }

    @DeleteMapping("/cache/all")
    public String clearRedisCache() {

        redisService.clearCache();

        return "Entire Redis cache cleared.";

    }
}