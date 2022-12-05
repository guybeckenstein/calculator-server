package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.json.Response;

public class Divide implements BinaryOperation {
    @Override
    public Response operation(int x, int y) {
        if (y == 0) {
            System.out.println("Error while performing operation Divide: division by 0");
            return new Response(-1, "Error while performing operation Divide: division by 0");
        }
        System.out.println(x / y);
        return new Response(x / y, "");
    }
}