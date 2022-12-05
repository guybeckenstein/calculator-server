package com.example.client;

import com.example.server.json.Arguments;
import com.example.server.json.IndependentCalculator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private final String server;
    private final RestTemplate rest;
    private final HttpHeaders headers;

    public RestClient(int port) {
        server = "http://localhost:" + port;
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    /** POST **/
    public ResponseEntity<String> testIndependentCalculation(IndependentCalculator body) {
        String uri = "/independent/calculate";
        try {
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            return rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** GET **/
    public ResponseEntity<String> testGetStackSize() {
        String uri = "/stack/size";
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        return rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
    }

    /** PUT **/
    public ResponseEntity<String> testAddArguments(Arguments body) {
        String uri = "/stack/arguments";
        try {
        String jsonBody = new ObjectMapper().writeValueAsString(body);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            return rest.exchange(server + uri, HttpMethod.PUT, requestEntity, String.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** GET **/
    public ResponseEntity<String> testPerformOperation(String operation) {
        String uri = String.format("/stack/operate?operation=%s", operation);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        return rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
    }

    /** DELETE **/
    public ResponseEntity<String> testRemoveStackArguments(int count) {
        String uri = String.format("/stack/arguments?count=%s", count);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        return rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, String.class);
    }

}
