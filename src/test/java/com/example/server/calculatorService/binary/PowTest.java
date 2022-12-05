package com.example.server.calculatorService.binary;

import com.example.server.calculatorService.CalculateBinary;
import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PowTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.POW.getOperationObject();
    }

    @ParameterizedTest(name = "{0} ^ {1} = {2}")
    @CsvSource({
            "0, 0, 1, ",
            "100, 1, 100, ",
            "10, 2, 100, ",
            "10, -1, 0, "
    })
    void powOperation(int x, int y, int result, String errorMessage) {
        if (errorMessage == null)
            errorMessage = "";
        assertEquals(new ResponseJson(result, errorMessage), calculateBinary.operation(x, y),
                "Expected result -> " + result + "; got instead -> " + calculateBinary.operation(x, y)
        );
    }
}