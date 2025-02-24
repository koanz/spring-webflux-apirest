package com.idea.springboot.webflux.app.services.impl;

import com.idea.springboot.webflux.app.exceptions.CategoryNotFountException;
import com.idea.springboot.webflux.app.mappers.CategoryMapper;
import com.idea.springboot.webflux.app.models.documents.Category;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.repositories.CategoryRepository;
import com.idea.springboot.webflux.app.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryMapper mapper;

    @Override
    public Mono<CategoryDTO> findDTOById(String id) {
        return repository.findById(id).map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new CategoryNotFountException(id)));
    }

    @Override
    public Mono<Category> findById(String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new CategoryNotFountException(id)));
    }

    @Override
    public Flux<CategoryDTO> getAll() {
        return repository.findAll().map(mapper::toDTO);
    }

    @Transactional
    @Override
    public Mono<CategoryDTO> save(CategoryDTO request) {
        logger.info("Creating Product: " + request.getName());
        Category category = mapper.toEntity(request);
        category.setCreatedAt(new Date());

        return repository.save(category)
                .map(mapper::toDTO);
    }

    @Transactional
    @Override
    public Mono<CategoryDTO> update(String id, CategoryDTO request) {
        return null;
    }

    @Transactional
    @Override
    public Mono<Void> delete(String id) {
        logger.info("Deleting Product: " + id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFountException(id)))
                .flatMap(product -> repository.delete(product))
                .doOnError(throwable -> logger.error("Error in delete method", throwable));
    }

}
