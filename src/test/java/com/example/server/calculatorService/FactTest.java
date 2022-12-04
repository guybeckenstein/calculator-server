package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactTest {
    private UnaryOperation calculateUnary;

    @BeforeEach
    void setup() {
        calculateUnary = CalculateUnary.FACT.getOperationObject();
    }

    @Test
    void inputWithExceptionRaised() {
        assertEquals(
                new ResponseJson(-1, "Error while performing operation Factorial: not supported for the negative number"),
                calculateUnary.operation(-1)
        );
    }

    @Test
    void inputWithoutExceptions() {
        assertEquals(new ResponseJson(1, ""), calculateUnary.operation(0));
        assertEquals(new ResponseJson(1, ""), calculateUnary.operation(1));
        assertEquals(new ResponseJson(6, ""), calculateUnary.operation(3));
        assertEquals(new ResponseJson(120, ""), calculateUnary.operation(5));
    }
}