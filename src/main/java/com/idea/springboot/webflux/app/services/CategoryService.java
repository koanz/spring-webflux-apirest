package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.models.documents.Category;
import reactor.core.publisher.Mono;

public interface CategoryService {
    public Mono<Category> findById(String id);
}
