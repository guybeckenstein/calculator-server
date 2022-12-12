package com.example.client;

import com.example.server.ServerApplication;
import com.example.server.json.ArgumentsJson;
import com.example.server.json.IndependentCalculator;
import com.example.server.json.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ClientApplicationTest {
    private RestClient client;

    @BeforeEach
    void init() {
        client = new RestClient(9583);
    }

    @BeforeAll
    static void setup() {
        SpringApplication.run(ServerApplication.class);
    }

    @ParameterizedTest
    @MethodSource("negativeIndependentCalculationsProviderMethod")
    void testNegativeIndependentCalculations(int[] arguments, String operation, int result, String errorMessage) {
        try {
            client.testIndependentCalculation(new IndependentCalculator(arguments, operation));
        } catch (HttpClientErrorException e) {
            assertTrue(
                    e.getLocalizedMessage().contains(errorMessage) &&
                            e.getLocalizedMessage().contains(Integer.toString(result)),
                    "Exception message does not contain expected phrases");
            // Works as expected
        } catch (Exception e) {
            throw new RuntimeException("Expected HttpClientErrorException$Conflict, got " + e.getClass() + " instead");
        }
    }

    private static Stream<Arguments> negativeIndependentCalculationsProviderMethod() {
        return Stream.of(
                Arguments.of(new int[]{3, 1}, "multiply", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error: unknown operation: multiply\"}"),
                Arguments.of(new int[]{3, 0}, "Divide", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error while performing operation Divide: division by 0\"}"),
                Arguments.of(new int[]{5}, "PLUS", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error: Not enough arguments to perform the operation Plus\"}"),
                Arguments.of(new int[]{1, 2, 3}, "MiNuS", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error: Too many arguments to perform the operation Minus\"}"),
                Arguments.of(new int[]{-3}, "fact", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error while performing operation Factorial: not supported for the negative number\"}"),
                Arguments.of(new int[]{-5, 10}, "AbS", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error: Too many arguments to perform the operation Abs\"}"),
                Arguments.of(new int[]{}, "FACT", -1,
                        "409 : \"{\"result\":-1,\"error-message\":\"Error: Not enough arguments to perform the operation Fact\"}")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveIndependentCalculationsProviderMethod")
    void testPositiveIndependentCalculations(int[] arguments, String operation, int result, String errorMessage) throws JsonProcessingException {
        ResponseEntity<String> response = client.testIndependentCalculation(new IndependentCalculator(arguments, operation));
        Response expectedResponse = new Response(result, errorMessage);
        String expectedResponseJson = new ObjectMapper().writeValueAsString(expectedResponse);
        ResponseEntity<String> expectedResponseEntity = new ResponseEntity<>(expectedResponseJson, HttpStatus.OK);
        assertEquals(expectedResponseEntity.getBody(), response.getBody());
    }

    private static Stream<Arguments> positiveIndependentCalculationsProviderMethod() {
        return Stream.of(
                Arguments.of(new int[]{3, 5}, "minus", -2, ""),
                Arguments.of(new int[]{-1}, "abs", 1, "")
        );
    }

    @SafeVarargs
    @ParameterizedTest
    @MethodSource("positiveStackCalculationsProviderMethod")
    final void testPositiveStackCalculations(ResponseEntity<String>... results) {
        ResponseEntity<String> getStackSize1 = client.testGetStackSize();
        assertEquals(results[0].getBody(), getStackSize1.getBody(),
                "Failed in test #1 of stack calculations");
        ResponseEntity<String> addArguments1 = client.testAddArguments(new ArgumentsJson(new int[]{3, 4}));
        assertEquals(results[1].getBody(), addArguments1.getBody(),
                "Failed in test #2 of stack calculations");
        ResponseEntity<String> getStackSize2 = client.testGetStackSize();
        assertEquals(results[2].getBody(), getStackSize2.getBody(),
                "Failed in test #3 of stack calculations");
        ResponseEntity<String> removeArguments1 = client.testRemoveStackArguments(1);
        assertEquals(results[3].getBody(), removeArguments1.getBody(),
                "Failed in test #4 of stack calculations");
        ResponseEntity<String> testPerformOperation1 = client.testPerformOperation("fact");
        assertEquals(results[4].getBody(), testPerformOperation1.getBody(),
                "Failed in test #5 of stack calculations");
        ResponseEntity<String> getStackSize3 = client.testGetStackSize();
        assertEquals(results[5].getBody(), getStackSize3.getBody(),
                "Failed in test #6 of stack calculations");
    }

    private static Stream<Arguments> positiveStackCalculationsProviderMethod() throws JsonProcessingException {
        return Stream.of(
                Arguments.of((Object)new ResponseEntity[]
                        {
                                new ResponseEntity<>(new ObjectMapper().writeValueAsString(new Response(0, "")), HttpStatus.OK),
                                new ResponseEntity<>(new ObjectMapper().writeValueAsString(new Response(2, "")), HttpStatus.OK),
                                new ResponseEntity<>(new ObjectMapper().writeValueAsString(new Response(2, "")), HttpStatus.OK),
                                new ResponseEntity<>(new ObjectMapper().writeValueAsString(new Response(1, "")), HttpStatus.OK),
                                new ResponseEntity<>(new ObjectMapper().writeValueAsString(new Response(6, "")), HttpStatus.OK),
                                new ResponseEntity<>(new ObjectMapper().writeValueAsString(new Response(0, "")), HttpStatus.OK)
                        }
                )
        );
    }
}