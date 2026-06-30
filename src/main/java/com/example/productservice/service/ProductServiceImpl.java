package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Product save(Product product) {

        log.info("Saving product into Database: {}", product);

        return repository.save(product);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {

        log.info("Cache MISS - Fetching Product {} from Database", id);

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found : " + id));
    }

    @Override
    public List<Product> getAllProducts() {

        log.info("Fetching all products from Database");

        return repository.findAll();
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public Product update(Long id, Product product) {

        log.info("Updating Product {} in Database and Redis Cache", id);

        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found : " + id));

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());

        return repository.save(existing);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {

        log.info("Deleting Product {} from Database and Evicting Cache", id);

        repository.deleteById(id);
    }
}