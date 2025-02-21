package com.idea.springboot.webflux.app.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductDTO {
    private String id;

    @NotEmpty(message = "Field name cannot be empty.")
    private String name;

    @NotNull(message = "Field price cannot be empty.")
    @Positive(message = "Field price must be greater than zero (0).")
    private Double price;

    @NotNull
    @JsonProperty("category")
    private CategoryDTO categoryDTO;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("created_at")
    private Date createdAt;

    public ProductDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
