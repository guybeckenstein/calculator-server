package com.example.server.calculatorService.interfaces;

import com.example.server.json.ResponseJson;

public interface UnaryOperation {
    ResponseJson operation(int x);
}
