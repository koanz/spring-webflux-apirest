package com.idea.springboot.webflux.app.handler;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import com.idea.springboot.webflux.app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Locale;

@Component
public class CategoryHandler {
    @Autowired
    private CategoryService service;

    @Autowired
    private MessageSource messageSource;

    @Value("${category.field.name.message}")
    private String FIELD_NAME;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(service.getAll(), ProductDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return service.findById(request.pathVariable("id"))
                .flatMap(category -> ServerResponse.ok().bodyValue(category))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<CategoryDTO> categoryDtoMono = request.bodyToMono(CategoryDTO.class);
        return categoryDtoMono.flatMap(category -> service.save(category)
                .flatMap(savedCategory -> {
                    MessageResponse response = new MessageResponse(
                            messageSource.getMessage("creation.message", null, Locale.getDefault()),
                            new Date().toString());
                    response.addDynamicFieldName(FIELD_NAME, savedCategory);
                    return ServerResponse.ok().bodyValue(response);
                }));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<CategoryDTO> categoryDtoMono = request.bodyToMono(CategoryDTO.class);
        return categoryDtoMono.flatMap(categoryDTO -> service.update(request.pathVariable("id"), categoryDTO)
                .flatMap(updatedProduct -> {
                    MessageResponse response = new MessageResponse(
                            messageSource.getMessage("update.message", null, Locale.getDefault()),
                            new Date().toString());
                    response.addDynamicFieldName(FIELD_NAME, updatedProduct);
                    return ServerResponse.ok().bodyValue(response);
                }));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return service.delete(request.pathVariable("id"))
                .then(ServerResponse.ok().bodyValue(new MessageResponse(
                        messageSource.getMessage("delete.message", null, Locale.getDefault()),
                        new Date().toString())));
    }
}
