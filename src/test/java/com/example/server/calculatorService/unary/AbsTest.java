package com.example.server.calculatorService.unary;

import com.example.server.calculatorService.CalculateUnary;
import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AbsTest {
    private UnaryOperation calculateUnary;

    @BeforeEach
    void setup() {
        calculateUnary = CalculateUnary.ABS.getOperationObject();
    }

    @ParameterizedTest(name = "|{0}| = {1}")
    @CsvSource({
            "4, 4, ",
            "-0, 0, ",
            "0, 0, ",
            "-190, 190, "
    })
    void absoluteValue(int x, int result, String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }
        assertEquals(
                new Response(result, errorMessage),
                calculateUnary.operation(x),
                "Expected error message, got " + result + " instead"
        );
    }
}