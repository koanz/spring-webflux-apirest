package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.services.CategoryService;
import com.idea.springboot.webflux.app.validations.OnCreate;
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
@RequestMapping("/api/v1/categories")
public class CategoryRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", "/list"})
    public Mono<ResponseEntity<Flux<CategoryDTO>>> getAll() {
        return Mono.just(ResponseEntity.ok(categoryService.getAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CategoryDTO>> findById(@PathVariable String id) {
        return categoryService.findDTOById(id).map(ResponseEntity::ok);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<MessageResponse>> create(@RequestBody @Validated(OnCreate.class) CategoryDTO request) {
        Date currentDate = new Date();

        return categoryService.save(request).map(responseDto -> {
            MessageResponse response = new MessageResponse("Successfully created", currentDate.toString());
            response.addDynamicField("category", responseDto);

            return ResponseEntity.ok(response);
        });
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<MessageResponse>> delete(@PathVariable String id) {
        Date currentDate = new Date();

        return categoryService.delete(id)
                .then(Mono.just(ResponseEntity.ok(new MessageResponse("Successfully deleted", currentDate.toString()))));
    }
}
