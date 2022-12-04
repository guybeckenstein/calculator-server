package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivideTest {
    private BinaryOperation calculateBinary;

    @BeforeEach
    void setup() {
        calculateBinary = CalculateBinary.DIVIDE.getOperationObject();
    }

    @Test
    void divisionWithoutCarry() {
        assertEquals(new ResponseJson(1, ""), calculateBinary.operation(-1, -1));
        assertEquals(new ResponseJson(-2, ""), calculateBinary.operation(4, -2));
        assertEquals(new ResponseJson(4, ""), calculateBinary.operation(8, 2));
        assertEquals(new ResponseJson(3, ""), calculateBinary.operation(15, 5));
    }

    @Test
    void divisionWithCarry() {
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(-1, -2));
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(1, -2));
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(-1, 2));
        assertEquals(new ResponseJson(0, ""), calculateBinary.operation(1, 2));
        assertEquals(new ResponseJson(10, ""), calculateBinary.operation(31, 3));
        assertEquals(new ResponseJson(-10, ""), calculateBinary.operation(31, -3));
    }

    @Test
    void divisionByZero() {
        assertEquals(
                new ResponseJson(-1, "Error while performing operation Divide: division by 0"),
                calculateBinary.operation(0, 0)
        );
        assertEquals(

                new ResponseJson(-1, "Error while performing operation Divide: division by 0"),
                calculateBinary.operation(1_000, 0)
        );
    }
}