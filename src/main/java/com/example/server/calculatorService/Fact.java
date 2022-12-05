package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.Response;

public class Fact implements UnaryOperation {
    @Override
    public Response operation(int x) {
        if (x < 0) {
            System.out.println("Error while performing operation Factorial: not supported for the negative number");
            return new Response(-1, "Error while performing operation Factorial: not supported for the negative number");
        }
        int fact = 1;
        for (int i = x; i >= 2; i--) {
            fact *= x--;
        }
        System.out.println(fact);
        return new Response(fact, "");
    }
}
