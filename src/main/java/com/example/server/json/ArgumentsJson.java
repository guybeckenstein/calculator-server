package com.example.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArgumentsJson(int[] arguments) {
    public ArgumentsJson(@JsonProperty("arguments") int[] arguments) {
        this.arguments = arguments;
    }
}
