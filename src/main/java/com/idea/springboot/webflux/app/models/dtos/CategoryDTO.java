package com.idea.springboot.webflux.app.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idea.springboot.webflux.app.validations.OnCreate;
import com.idea.springboot.webflux.app.validations.OnUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CategoryDTO {

    @NotNull(message = "Field id cannot be empty.", groups = OnCreate.class)
    private String id;

    @NotEmpty(message = "Field name cannot be empty.", groups = OnUpdate.class)
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("created_at")
    private Date createdAt;

    public CategoryDTO() {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
