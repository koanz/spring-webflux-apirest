package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.Product;
import com.idea.springboot.webflux.app.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/list"})
    public Mono<ResponseEntity<Flux<Product>>> getAll() {
        return Mono.just(ResponseEntity.ok(productService.getAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Product>>> findById(@PathVariable String id) {
        return Mono.just(ResponseEntity.ok(productService.findById(id)));
    }
}
