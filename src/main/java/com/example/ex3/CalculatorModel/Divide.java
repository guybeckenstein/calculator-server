package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.BinaryOperation;
import com.example.ex3.json.ResponseJson;

public class Divide implements BinaryOperation {
    @Override
    public ResponseJson operation(int x, int y) {
        if (y == 0) {
            System.out.println("Error while performing operation Divide: division by 0");
            return new ResponseJson(-1, "Error while performing operation Divide: division by 0");
        }
        System.out.println(x / y);
        return new ResponseJson(x / y, "");
    }
}