package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.BinaryOperation;
import com.example.ex3.json.ResponseJson;

public class Pow implements BinaryOperation {
    @Override
    public ResponseJson operation(int x, int y) {
        System.out.println((int) Math.pow(x, y));
        return new ResponseJson((int) Math.pow(x, y), "");
    }
}