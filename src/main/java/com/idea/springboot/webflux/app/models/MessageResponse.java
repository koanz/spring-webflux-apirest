package com.idea.springboot.webflux.app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageResponse {
    private String message;
    private String timestamp;

    @JsonProperty("product")
    private ProductDTO productDTO;

    public MessageResponse() {
    }

    public MessageResponse(String message, String timestamp, ProductDTO productDTO) {
        this.message = message;
        this.timestamp = timestamp;
        this.productDTO = productDTO;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
