package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.Response;

public class Pow implements BinaryOperation {
    @Override
    public Response operation(int x, int y) {
        System.out.println((int) Math.pow(x, y));
        return new Response((int) Math.pow(x, y), "");
    }
}