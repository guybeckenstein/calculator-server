package com.example.server.calculatorService.binary;

import com.example.server.calculatorService.CalculateBinary;
import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DivideTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.DIVIDE.getOperationObject();
    }

    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({
            "-1, -1, 1",
            "4, -2, -2",
            "8, 2, 4",
            "15, 5, 3",
            "-1, -2, 0",
            "1, -2, 0",
            "-1, 2, 0",
            "1, 2, 0",
            "31, 3, 10",
            "31, -4, -7"
    })
    void divisionWithoutAndWithCarry(int x, int y, int result) {
        assertEquals(new Response(result,""), calculateBinary.operation(x, y),
                "Expected result -> " + result + "; got instead -> " + calculateBinary.operation(x, y)
        );
    }

    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({
            "0, 0, -1, Error while performing operation Divide: division by 0",
            "1, 0, -1, Error while performing operation Divide: division by 0",
            "1_000, 0, -1, Error while performing operation Divide: division by 0"
    })
    void divisionByZero(int x, int y, int result, String errorMessage) {
        assertEquals(
                new Response(result, errorMessage),
                calculateBinary.operation(x, y),
                "Expected error message, got " + result + " instead"
        );
    }
}