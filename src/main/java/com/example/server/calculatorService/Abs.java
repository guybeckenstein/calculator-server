package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.ResponseJson;

public class Abs implements UnaryOperation {
    @Override
    public ResponseJson operation(int x) {
        x = x > 0 ? x : (x * -1);
        System.out.println(x);
        return new ResponseJson(x, "");
    }
}