package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.mappers.ProductMapper;
import com.idea.springboot.webflux.app.models.documents.Product;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Flux<ProductDTO> getAll() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Mono<ProductDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Transactional
    @Override
    public Mono<ProductDTO> save(ProductDTO request) {
        logger.info("Creating Product: " + request.getName());

        return categoryService.findById(request.getCategoryDTO().getId())
                .flatMap(category -> {
                    Product product = mapper.toEntity(request);
                    product.setCategory(category);
                    product.setCreatedAt(new Date());
                    return repository.save(product);
                })
                .map(mapper::toDto)
                .doOnError(throwable -> logger.error("Error in save method"));
    }

    @Transactional
    @Override
    public Mono<ProductDTO> update(String id, ProductDTO request) {
        logger.info("Updating Product: " + id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found id: " + id)))
                .flatMap(existingProduct -> {
                    return categoryService.findById(request.getCategoryDTO().getId())
                            .flatMap(category -> {
                                existingProduct.setName(request.getName());
                                existingProduct.setPrice(request.getPrice());
                                existingProduct.setCategory(category);
                                return repository.save(existingProduct);
                            });
                })
                .map(mapper::toDto)
                .doOnError(throwable -> logger.error("Error in update method"));
    }

    @Transactional
    @Override
    public Mono<Void> delete(String id) {
        logger.info("Deleting Product: " + id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found id: " + id)))
                .flatMap(product -> repository.delete(product))
                .doOnError(throwable -> logger.error("Error in delete method", throwable));

    }
}
