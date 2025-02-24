package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.services.ProductService;
import com.idea.springboot.webflux.app.validations.OnProductCreate;
import com.idea.springboot.webflux.app.validations.OnProductUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<ResponseEntity<MessageResponse>> save(@RequestBody @Validated(OnProductCreate.class) ProductDTO request) {
        Date currentDate = new Date();

        return productService.save(request).map(responseDto -> {
            MessageResponse response = new MessageResponse("Successfully created", currentDate.toString());
            response.addDynamicField("product", responseDto);

            return ResponseEntity.ok(response);
        });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<MessageResponse>> update(@PathVariable String id, @Validated(OnProductUpdate.class) @RequestBody ProductDTO request) {
        Date currentDate = new Date();

        return productService.update(id, request).map(productDTO -> {
            MessageResponse response = new MessageResponse("Successfully created", currentDate.toString());
            response.addDynamicField("product", productDTO);

            return ResponseEntity.ok(response);
        });
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<MessageResponse>> delete(@PathVariable String id) {
        Date currentDate = new Date();

        return productService.delete(id)
                .then(Mono.just(ResponseEntity.ok(new MessageResponse("Successfully deleted", currentDate.toString()))));
    }

}
