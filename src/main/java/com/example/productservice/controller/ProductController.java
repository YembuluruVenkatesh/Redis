package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

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
}