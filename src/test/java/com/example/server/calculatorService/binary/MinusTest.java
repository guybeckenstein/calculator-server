package com.example.server.calculatorService.binary;

import com.example.server.calculatorService.CalculateBinary;
import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MinusTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.MINUS.getOperationObject();
    }

    @ParameterizedTest(name = "{0} - {1} = {2}")
    @CsvSource({
            "0, 0, 0, ",
            "100, 0, 100, ",
            "50, 50, 0, ",
            "15, 5, 10, ",
            "-0, -0, 0, ",
            "-100, 0, -100, ",
            "-50, 50, -100, ",
            "-50, -50, 0, "
    })
    void positiveAndNegativeValues(int x, int y, int result, String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }
        assertEquals(new ResponseJson(result, errorMessage), calculateBinary.operation(x, y),
                "Expected result -> " + result + "; got instead -> " + calculateBinary.operation(x, y)
        );
    }
}