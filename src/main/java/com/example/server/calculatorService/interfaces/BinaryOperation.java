package com.example.server.calculatorService.interfaces;

import com.example.server.json.Response;

public interface BinaryOperation {
    Response operation(int x, int y);
}
