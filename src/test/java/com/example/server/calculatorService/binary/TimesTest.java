package com.example.server.calculatorService.binary;

import com.example.server.calculatorService.CalculateBinary;
import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TimesTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.TIMES.getOperationObject();
    }

    @ParameterizedTest(name = "{0} * {1} = {2}")
    @CsvSource({
            "10, 3, 30, ",
            "5, 5, 25, ",
            "0, 0, 0, ",
            "0, 10, 0, ",
            "-13, -3, 39, ",
            "-50, 10, -500, "
    })
    void multiply(int x, int y, int result, String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }
        assertEquals(new Response(result, errorMessage), calculateBinary.operation(x, y));
    }
}