package com.example.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArgumentsJson {
    private final int[] arguments;

    public ArgumentsJson(@JsonProperty("arguments") int[] arguments) {
        this.arguments = arguments;
    }

    public int[] getArguments() {
        return arguments;
    }
}
