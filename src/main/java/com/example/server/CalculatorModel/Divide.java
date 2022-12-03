package com.example.server.CalculatorModel;

import com.example.server.CalculatorModel.interfaces.BinaryOperation;
import com.example.server.json.ResponseJson;

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