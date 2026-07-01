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

    @Override
    public Product save(Product product) {

        Product savedProduct = repository.save(product);

        redisService.saveProduct(savedProduct);

        log.info("Saved Product {} into Database", savedProduct.getId());

        return savedProduct;
    }

    @Override
    public Product getProduct(Long id) {

        Product cachedProduct = redisService.getProduct(id);

        if (cachedProduct != null) {

            log.info("Returning Product {} from Redis", id);

            return cachedProduct;
        }

        log.info("Fetching Product {} from Database", id);

        Product product = (Product) repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id));

        redisService.saveProduct(product);

        return product;
    }

    @Override
    public List<Product> getAllProducts() {

        log.info("Fetching all products from Database");

        return repository.findAll();
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

        log.info("Updated Product {} in Database and Redis", id);

        return updated;
    }

    @Override
    public void delete(Long id) {

        repository.deleteById(id);

        redisService.deleteProduct(id);

        log.info("Deleted Product {} from Database and Redis", id);

    }
}