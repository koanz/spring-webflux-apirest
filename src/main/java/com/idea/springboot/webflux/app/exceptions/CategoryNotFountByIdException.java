package com.idea.springboot.webflux.app.exceptions;

public class CategoryNotFountException extends RuntimeException {
    public CategoryNotFountException(String id) {
        super("Category with id '" + id + "' not found");
    }
}
