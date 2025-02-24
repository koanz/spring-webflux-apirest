package com.idea.springboot.webflux.app.config;

import com.idea.springboot.webflux.app.handler.CategoryHandler;
import com.idea.springboot.webflux.app.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        return RouterFunctions.route()
                .GET("/api/v2/products", productHandler::getAll)
                .GET("/api/v2/products/", productHandler::getAll)
                .GET("/api/v2/products/{id}", productHandler::findById)
                .POST("/api/v2/products/create", productHandler::save)
                .PUT("/api/v2/products/update/{id}", productHandler::update)
                .DELETE("/api/v2/products/delete/{id}", productHandler::delete)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> categoryRoutes(CategoryHandler categoryHandler) {
        return RouterFunctions.route()
                .GET("/api/v2/categories", categoryHandler::getAll)
                .GET("/api/v2/categories/", categoryHandler::getAll)
                .GET("/api/v2/categories/{id}", categoryHandler::findById)
                .POST("/api/v2/categories/create", categoryHandler::save)
                .PUT("/api/v2/categories/update/{id}", categoryHandler::update)
                .DELETE("/api/v2/categories/delete/{id}", categoryHandler::delete)
                .build();
    }
}
