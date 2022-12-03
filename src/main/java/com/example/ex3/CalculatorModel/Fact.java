package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.UnaryOperation;
import com.example.ex3.json.ResponseJson;

public class Fact implements UnaryOperation {
    @Override
    public ResponseJson operation(int x) {
        if (x < 0) {
            System.out.println("Error while performing operation Factorial: not supported for the negative number");
            return new ResponseJson(-1, "Error while performing operation Factorial: not supported for the negative number");
        }
        int fact = 1;
        for (int i = x; i >= 2; i--) {
            fact *= x--;
        }
        System.out.println(fact);
        return new ResponseJson(fact, "");
    }
}