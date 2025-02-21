package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.services.ProductService;
import com.idea.springboot.webflux.app.validations.OnCreate;
import com.idea.springboot.webflux.app.validations.OnUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/list"})
    public Mono<ResponseEntity<Flux<ProductDTO>>> getAll() {
        return Mono.just(ResponseEntity.ok(productService.getAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> findById(@PathVariable String id) {
        return productService.findById(id).map(ResponseEntity::ok);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<MessageResponse>> create(@Validated(OnCreate.class) @RequestBody ProductDTO request) {
        Date currentDate = new Date();
        return productService.save(request)
                .map(productDTO -> {
                    return ResponseEntity.ok(new MessageResponse("Successfully created", currentDate.toString(), productDTO));
                })
                .onErrorResume(error -> {
                    logger.error("Error while trying to create the Product.", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new MessageResponse("Error while trying to create the Product.", currentDate.toString(), null)));
                });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<MessageResponse>> update(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody ProductDTO request) {
        Date currentDate = new Date();
        return productService.update(id, request).map(productDTO -> {
                    return ResponseEntity.ok(new MessageResponse("Successfully updated", currentDate.toString(), productDTO));
                })
                .onErrorResume(error -> {
                    logger.error("Error while trying to update the Product.", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new MessageResponse("Error while trying to updated the Product.", currentDate.toString(), null)));
                });
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return productService.delete(id)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted.")))
                .doOnError(error -> logger.error("Error while trying to delete the Product.", error));
    }

}
