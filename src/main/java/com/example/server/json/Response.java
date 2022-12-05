package com.example.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Response(int result, String errorMessage) {
    public Response(@JsonProperty("result") int result,
                    @JsonProperty("error-message") String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object anObject) {
        Response otherResponse = (Response) anObject;
        if (errorMessage.isEmpty()) {
            return result == otherResponse.result();
        } else {
            return errorMessage.equals(otherResponse.errorMessage());
        }
    }
}
