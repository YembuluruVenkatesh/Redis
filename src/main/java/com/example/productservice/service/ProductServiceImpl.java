package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductRedisService redisService;
    private final ProductRecentService recentService;
    private final ProductRankingService rankingService;

    @Override
    public Product save(Product product) {

        Product savedProduct = repository.save(product);

        redisService.saveProduct(savedProduct);
        redisService.deleteAllProductsCache();
        log.info("Saved Product {} into Database", savedProduct.getId());

        return savedProduct;
    }

    @Override
    public Product getProduct(Long id) {

        Product cachedProduct = redisService.getProduct(id);

        if (cachedProduct != null) {

            log.info("Returning Product {} from Redis", id);
            recentService.addRecentProduct(id);
            rankingService.incrementView(id);
            return cachedProduct;
        }

        log.info("Fetching Product {} from Database", id);

        Product product = (Product) repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id));

        redisService.saveProduct(product);
        recentService.addRecentProduct(id);
        rankingService.incrementView(id);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {

        log.info("Fetching all products from Database");

        //return repository.findAll();
        List<Product> cachedProducts =
                 redisService.getAllProducts();

        if (cachedProducts != null) {

            log.info("Returning Product List from Redis");

            return cachedProducts;
        }

        log.info("Fetching Product List from Database");

        List<Product> products = repository.findAll();

        redisService.saveAllProducts(products);

        return products;
    }

    @Override
    public Product update(Long id, Product product) {

        Product existing = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id));

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());

        Product updated = repository.save(existing);

        redisService.saveProduct(updated);
        redisService.deleteAllProductsCache();
        log.info("Updated Product {} in Database and Redis", id);

        return updated;
    }

    @Override
    public void delete(Long id) {

        repository.deleteById(id);

        redisService.deleteProduct(id);
        redisService.deleteAllProductsCache();
        log.info("Deleted Product {} from Database and Redis", id);

    }

    @Override
    public List<Product> searchProducts(String keyword) {

        List<Product> cachedProducts =
                redisService.getSearchResults(keyword);

        if (cachedProducts != null) {

            log.info("Returning Search Result '{}' from Redis", keyword);

            return cachedProducts;
        }

        log.info("Searching '{}' in Database", keyword);

        List<Product> products =
                repository.findByNameContainingIgnoreCase(keyword);

        redisService.saveSearchResults(keyword, products);

        return products;
    }
}