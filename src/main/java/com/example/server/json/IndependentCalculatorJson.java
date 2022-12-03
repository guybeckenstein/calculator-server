package com.example.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndependentCalculatorJson {
    private final int[] arguments;
    private final String operation;

    public IndependentCalculatorJson(@JsonProperty("arguments") int[] arguments,
                                     @JsonProperty("operation") String operation) {
        this.arguments = arguments;
        this.operation = operation;
    }

    public int[] getArguments() {
        return arguments;
    }

    public String getOperation() {
        return operation;
    }
}
