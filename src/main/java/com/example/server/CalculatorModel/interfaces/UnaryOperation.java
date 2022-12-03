package com.example.server.CalculatorModel.interfaces;

import com.example.server.json.ResponseJson;

public interface UnaryOperation {
    ResponseJson operation(int x);
}
