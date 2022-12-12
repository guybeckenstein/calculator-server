package com.example.server.calculatorService.binary;

import com.example.server.calculatorService.CalculateBinary;
import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DivideTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.DIVIDE.getOperationObject();
    }

    @ParameterizedTest(name = "{0} / {1} = {2}")
    @MethodSource("providerMethod")
    void division(int x, int y, int result, String errorMessage) {
        assertEquals(
                new Response(result, errorMessage),
                calculateBinary.operation(x, y),
                "Expected error message, got " + result + " instead"
        );
    }

    private static Stream<Arguments> providerMethod() {
        return Stream.of(
                Arguments.of(-1, -1, 1, ""),
                Arguments.of(4, -2, -2, ""),
                Arguments.of(8, 2, 4, ""),
                Arguments.of(15, 5, 3, ""),
                Arguments.of(-1, -2, 0, ""),
                Arguments.of(1, -2, 0, ""),
                Arguments.of(1, -1, -1, ""),
                Arguments.of(31, 3, 10, ""),
                Arguments.of(43, -7, -6, ""),
                Arguments.of(0, 0, -1, "Error while performing operation Divide: division by 0"),
                Arguments.of(1, 0, -1, "Error while performing operation Divide: division by 0"),
                Arguments.of(1_000, 0, -1, "Error while performing operation Divide: division by 0")
        );
    }
}