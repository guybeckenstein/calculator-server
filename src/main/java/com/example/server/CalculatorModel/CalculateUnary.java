package com.example.server.CalculatorModel;

import com.example.server.CalculatorModel.interfaces.UnaryOperation;

public enum CalculateUnary {
    ABS {
        public UnaryOperation getOperationObject() { return new Abs(); }
    }, FACT {
        public UnaryOperation getOperationObject() { return new Fact(); }
    };

    public abstract UnaryOperation getOperationObject();
}
