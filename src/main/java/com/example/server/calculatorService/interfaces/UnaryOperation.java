package com.example.server.calculatorService.interfaces;

import com.example.server.json.Response;

public interface UnaryOperation {
    Response operation(int x);
}
