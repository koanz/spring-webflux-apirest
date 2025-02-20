package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    public Flux<Product> getAll();
    public Mono<Product> findById(String id);
    public Mono<Product> save(Product product);
}
