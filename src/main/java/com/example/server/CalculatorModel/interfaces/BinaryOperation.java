package com.example.server.CalculatorModel.interfaces;

import com.example.server.json.ResponseJson;

public interface BinaryOperation {
    ResponseJson operation(int x, int y);
}
