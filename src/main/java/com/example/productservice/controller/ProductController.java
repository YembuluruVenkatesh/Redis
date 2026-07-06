package com.example.productservice.controller;

import com.example.productservice.dto.PriceRequest;
import com.example.productservice.entity.Product;
import com.example.productservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;
    private final ProductRedisService redisService;
    private final ProductHashService productHashService;
    private final ProductRecentService recentService;
    private final ProductFavoriteService favoriteService;
    private final ProductRankingService rankingService;
    private final RedisTransactionService transactionService;

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

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam String name) {

        log.info("Searching Product : {}", name);

        List<Product> products =
                service.searchProducts(name);

        log.info("Found {} products", products.size());

        return products;
    }

    @PostMapping("/hash")
    public Product saveHash(@RequestBody Product product) {

        Product saved = service.save(product);

        productHashService.save(saved);

        return saved;
    }

    @GetMapping("/hash/{id}")
    public Product getHash(@PathVariable Long id) {

        return productHashService.get(id);
    }

    @PatchMapping("/hash/{id}/price")
    public String updatePrice(
            @PathVariable Long id,
            @RequestBody PriceRequest request) {

        productHashService.updatePrice(id, request.getPrice());

        return "Price Updated";
    }

    @DeleteMapping("/hash/{id}")
    public String deleteHash(@PathVariable Long id) {

        productHashService.delete(id);

        return "Hash Deleted";
    }

    @GetMapping("/recent")
    public List<Object> recentProducts() {

        return recentService.getRecentProducts();

    }

    @DeleteMapping("/recent")
    public String clearRecent() {

        recentService.clearRecentProducts();

        return "Recent Products Cleared";
    }

    @PostMapping("/{userId}/favorites/{productId}")
    public String addFavorite(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        favoriteService.addFavorite(userId, productId);

        return "Product added to favorites";
    }
    @DeleteMapping("/{userId}/favorites/{productId}")
    public String removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        favoriteService.removeFavorite(userId, productId);

        return "Product removed from favorites";
    }
    @GetMapping("/{userId}/favorites")
    public Set<Object> favorites(
            @PathVariable Long userId) {

        return favoriteService.getFavorites(userId);
    }
    @GetMapping("/{userId}/favorites/{productId}")
    public boolean isFavorite(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        return favoriteService.isFavorite(userId, productId);
    }
    @GetMapping("/{userId}/favorites/count")
    public Long count(
            @PathVariable Long userId) {

        return favoriteService.totalFavorites(userId);
    }

//    @GetMapping("/ranking")
//    public Set<Object> topProducts() {
//        return rankingService.topProducts(10);
//    }

    @GetMapping("/ranking")
    public Set<ZSetOperations.TypedTuple<Object>> topProducts() {
        return rankingService.topProducts(10);
    }
    @GetMapping("/ranking/{id}/score")
    public Double score(@PathVariable Long id) {
        return rankingService.getScore(id);
    }

    @GetMapping("/ranking/{id}/rank")
    public Long rank(@PathVariable Long id) {

        return rankingService.getRank(id);

    }

    @DeleteMapping("/ranking/{id}")
    public String deleteRanking(
            @PathVariable Long id) {

        rankingService.removeProduct(id);

        return "Removed";
    }

    @PostMapping("/transaction")
    public String transaction() {

        transactionService.executeTransaction();

        return "Transaction Executed Successfully";
    }
}