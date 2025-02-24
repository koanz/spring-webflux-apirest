package com.idea.springboot.webflux.app.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class MessageResponse implements GenericMessageResponse {
    private String message;
    private String timestamp;
    private Map<String, Object> data = new HashMap<>();

    public MessageResponse() {
    }

    public MessageResponse(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public void addDynamicFieldName(String key, Object value) {
        data.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
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
