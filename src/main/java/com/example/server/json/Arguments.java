package com.example.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Arguments(int[] arguments) {
    public Arguments(@JsonProperty("arguments") int[] arguments) {
        this.arguments = arguments;
    }
}
