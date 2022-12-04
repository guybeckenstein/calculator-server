package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlusTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.PLUS.getOperationObject();
    }

    @Test
    void positiveValues() {
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(0, 0));
        assertEquals(new ResponseJson(100, ""), calculateBinary.operation(100, 0));
        assertEquals(new ResponseJson(100, ""), calculateBinary.operation(50, 50));
        assertEquals(new ResponseJson(100, ""), calculateBinary.operation(50, (int)50.0));
    }

    @Test
    void negativeValues() {
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(-0, -0));
        assertEquals(new ResponseJson(-100, ""), calculateBinary.operation(-100, 0));
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(-50, 50));
        assertEquals(new ResponseJson(-100, ""), calculateBinary.operation(-50, -(int)50.0));
    }
}