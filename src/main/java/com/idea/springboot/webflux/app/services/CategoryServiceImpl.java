package com.idea.springboot.webflux.app.services;

import com.idea.springboot.webflux.app.models.documents.Category;
import com.idea.springboot.webflux.app.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository repository;

    @Override
    public Mono<Category> findById(String id) {
        return repository.findById(id);
    }
}
