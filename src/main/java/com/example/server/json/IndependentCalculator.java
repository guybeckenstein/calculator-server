package com.example.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IndependentCalculator(int[] arguments, String operation) {
    public IndependentCalculator(@JsonProperty("arguments") int[] arguments,
                                 @JsonProperty("operation") String operation) {
        this.arguments = arguments;
        this.operation = operation;
    }
}
