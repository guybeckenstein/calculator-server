package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.POW.getOperationObject();
    }

    @Test
    void powOperation() {
        assertEquals(new ResponseJson(1, ""), calculateBinary.operation(0, 0));
        assertEquals(new ResponseJson(100, ""), calculateBinary.operation(10, 2));
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(10, -1));
    }
}