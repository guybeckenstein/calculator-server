package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbsTest {
    private UnaryOperation calculateUnary;

    @BeforeEach
    void setup() {
        calculateUnary = CalculateUnary.ABS.getOperationObject();
    }

    @Test
    void positiveNumbersAbsoluteValue() {
        ResponseJson expectedValue = new ResponseJson(4, "");
        assertEquals(expectedValue, calculateUnary.operation(4));
        assertEquals(expectedValue, calculateUnary.operation((int)4.5));
    }

    @Test
    void negativeNumbersAbsoluteValue() {
        ResponseJson expectedValue = new ResponseJson(4, "");
        assertEquals(expectedValue, calculateUnary.operation(-4));
        assertEquals(expectedValue, calculateUnary.operation((int)-4.5));
    }

    @Test
    void zeroAbsoluteValue() {
        ResponseJson expectedValue = new ResponseJson(0, "");
        assertEquals(expectedValue, calculateUnary.operation(0));
        assertEquals(expectedValue, calculateUnary.operation(-0));
    }
}