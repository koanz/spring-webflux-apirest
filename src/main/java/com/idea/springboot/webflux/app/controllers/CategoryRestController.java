package com.idea.springboot.webflux.app.controllers;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.services.CategoryService;
import com.idea.springboot.webflux.app.validations.OnCreate;
import com.idea.springboot.webflux.app.validations.OnProductUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Value("${category.field.name.message}")
    private String FIELD_NAME;

    @Autowired
    private MessageSource messageSource;

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
            MessageResponse response = new MessageResponse(messageSource.getMessage("creation.message", null, Locale.getDefault()), currentDate.toString());
            response.addDynamicFieldName(FIELD_NAME, responseDto);

            return ResponseEntity.ok(response);
        });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<MessageResponse>> update(@PathVariable String id, @Validated(OnProductUpdate.class) @RequestBody CategoryDTO request) {
        Date currentDate = new Date();

        return categoryService.update(id, request).map(responseDto -> {
            MessageResponse response = new MessageResponse(messageSource.getMessage("update.message", null, Locale.getDefault()), currentDate.toString());
            response.addDynamicFieldName(FIELD_NAME, responseDto);

            return ResponseEntity.ok(response);
        });
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<MessageResponse>> delete(@PathVariable String id) {
        Date currentDate = new Date();

        return categoryService.delete(id)
                .then(Mono.just(ResponseEntity.ok(new MessageResponse(messageSource.getMessage("update.message", null, Locale.getDefault()), currentDate.toString()))));
    }
}
