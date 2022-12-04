package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimesTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.TIMES.getOperationObject();
    }

    @Test
    void twoPositiveValues() {
        assertEquals(new ResponseJson(30, ""), calculateBinary.operation(10, 3));
        assertEquals(new ResponseJson(30, ""), calculateBinary.operation(3, 10));
    }

    @Test
    void multiplyingByZero() {
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(0, 0));
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(0, 10));
    }

    @Test
    void multiplyingNegativeValues() {
        assertEquals(new ResponseJson(39, ""), calculateBinary.operation(-13, -3));
        assertEquals(new ResponseJson(-500, ""), calculateBinary.operation(-50, 10));
    }
}