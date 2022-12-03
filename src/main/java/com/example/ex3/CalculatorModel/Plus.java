package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.BinaryOperation;
import com.example.ex3.json.ResponseJson;

public class Plus implements BinaryOperation {
    @Override
    public ResponseJson operation(int x, int y) {
        System.out.println(x + y);
        return new ResponseJson(x + y, "");
    }
}