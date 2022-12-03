package com.example.server.CalculatorModel;

import com.example.server.CalculatorModel.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;

public class Pow implements BinaryOperation {
    @Override
    public ResponseJson operation(int x, int y) {
        System.out.println((int) Math.pow(x, y));
        return new ResponseJson((int) Math.pow(x, y), "");
    }
}