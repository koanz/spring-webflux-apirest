package com.idea.springboot.webflux.app.repositories;

import com.idea.springboot.webflux.app.models.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
