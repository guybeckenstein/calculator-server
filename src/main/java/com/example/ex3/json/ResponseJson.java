package com.example.ex3.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseJson {
    private final int result;
    private final String errorMessage;

    public ResponseJson(@JsonProperty("result") int result,
                        @JsonProperty("error-message") String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public int getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
