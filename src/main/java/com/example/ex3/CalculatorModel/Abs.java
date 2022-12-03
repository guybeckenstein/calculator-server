package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.UnaryOperation;
import com.example.ex3.json.ResponseJson;

public class Abs implements UnaryOperation {
    @Override
    public ResponseJson operation(int x) {
        x = x > 0 ? x : (x * -1);
        System.out.println(x);
        return new ResponseJson(x, "");
    }
}