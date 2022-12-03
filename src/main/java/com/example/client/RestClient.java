package com.example.client;

import com.example.server.json.ArgumentsJson;
import com.example.server.json.IndependentCalculatorJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestClient {
    private String server;
    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatusCode status;

    public RestClient(int port) {
        server = "http://localhost:" + port;
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    /** POST **/
    public ResponseEntity<String> testIndependentCalculation(IndependentCalculatorJson body) {
        String uri = "/independent/calculate";
        try {
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
            this.setStatus(responseEntity.getStatusCode());
            return responseEntity;
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** GET **/
    public ResponseEntity<String> testGetStackSize() {
        String uri = "/stack/size";
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity;
    }

    /** PUT **/
    public ResponseEntity<String> testAddArguments(ArgumentsJson body) {
        String uri = "/stack/arguments";
        try {
        String jsonBody = new ObjectMapper().writeValueAsString(body);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.PUT, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity;
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** GET **/
    public ResponseEntity<String> testPerformOperation(String operation) {
        String uri = String.format("/stack/operate?operation=%s", operation);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity;
    }

    /** DELETE **/
    public ResponseEntity<String> testRemoveStackArguments(int count) {
        String uri = String.format("/stack/arguments?count=%s", count);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public void setStatus(HttpStatusCode status) {
        this.status = status;
    }
}
