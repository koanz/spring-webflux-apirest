package com.idea.springboot.webflux.app.config;

import com.idea.springboot.webflux.app.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                .GET("/api/v2/products", handler::getAll)
                .GET("/api/v2/products/{id}", handler::findById)
                .POST("/api/v2/products/create", handler::save)
                .PUT("/api/v2/products/update/{id}", handler::update)
                .DELETE("/api/v2/products/delete/{id}", handler::delete)
                .build();
    }
}
