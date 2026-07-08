package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    /**
     * CREATE PRODUCT
     */
    @Override
    @Caching(
            put = {
                    @CachePut(
                            value = "products",
                            key = "#result.id")
            },
            evict = {
                    @CacheEvict(
                            value = "allProducts",
                            allEntries = true)
            }
    )
    public Product save(Product product) {

        Product saved = repository.save(product);


        log.info("======================================");
        log.info("Product Saved Successfully");
        log.info("Product Id   : {}", saved.getId());
        log.info("Product Name : {}", saved.getName());
        log.info("======================================");

        return saved;
    }

    /**
     * GET PRODUCT BY ID
     */
    @Override
    @Cacheable(
            value = "products",
            key = "#id")
    public Product getProduct(Long id) {

        log.info("Fetching Product {} from Database", id);

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product Not Found : " + id));
    }

    /**
     * GET ALL PRODUCTS
     */
    @Override
    @Cacheable("allProducts")
    public List<Product> getAllProducts() {

        log.info("Fetching All Products From Database");

        return repository.findAll();
    }

    /**
     * UPDATE PRODUCT
     */
    @Override
    @Caching(
            put = {
                    @CachePut(
                            value = "products",
                            key = "#id")
            },
            evict = {
                    @CacheEvict(
                            value = "allProducts",
                            allEntries = true)
            }
    )
    public Product update(Long id, Product product) {

        Product existing =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Product Not Found : " + id));

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());

        Product updated = repository.save(existing);


        log.info("======================================");
        log.info("Product Updated Successfully");
        log.info("Product Id   : {}", updated.getId());
        log.info("======================================");

        return updated;
    }

    /**
     * DELETE PRODUCT
     */
    @Override
    @Caching(
            evict = {

                    @CacheEvict(
                            value = "products",
                            key = "#id"),

                    @CacheEvict(
                            value = "allProducts",
                            allEntries = true)
            }
    )
    public void delete(Long id) {

        Product product =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Product Not Found : " + id));

        repository.delete(product);



        log.info("======================================");
        log.info("Product Deleted Successfully");
        log.info("Product Id : {}", id);
        log.info("======================================");
    }

}