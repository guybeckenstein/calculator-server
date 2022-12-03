package com.example.server.CalculatorModel;

import com.example.server.CalculatorModel.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;

public class Minus implements BinaryOperation {
    @Override
    public ResponseJson operation(int x, int y) {
        System.out.println(x - y);
        return new ResponseJson(x - y, "");
    }
}