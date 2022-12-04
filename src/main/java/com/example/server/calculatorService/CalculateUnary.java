package com.example.server.calculatorService;

import com.example.server.calculatorService.interfaces.UnaryOperation;

public enum CalculateUnary {
    ABS {
        public UnaryOperation getOperationObject() { return new Abs(); }
    }, FACT {
        public UnaryOperation getOperationObject() { return new Fact(); }
    };

    public abstract UnaryOperation getOperationObject();
}
