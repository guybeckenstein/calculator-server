package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.Response;

public class Times implements BinaryOperation {
    @Override
    public Response operation(int x, int y) {
        return new Response(x * y, "");
    }
}