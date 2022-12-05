package com.example.server.calculatorService.unary;

import com.example.server.calculatorService.CalculateUnary;
import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class FactTest {
    private UnaryOperation calculateUnary;

    @BeforeEach
    void setup() {
        calculateUnary = CalculateUnary.FACT.getOperationObject();
    }

    @ParameterizedTest(name = "{0}! = {1}")
    @CsvSource({
            "4, 24, ",
            "1, 1, ",
            "0, 1, ",
            "5, 120, ",
            "-1, -1, Error while performing operation Factorial: not supported for the negative number",
            "-5, -1, Error while performing operation Factorial: not supported for the negative number"
    })
    void inputWithExceptionRaised(int x, int result, String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }
        assertEquals(
                new ResponseJson(result, errorMessage),
                calculateUnary.operation(x),
                "Expected error message, got " + result + " instead"
        );
    }
}