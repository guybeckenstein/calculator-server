package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.UnaryOperation;

public enum CalculateUnary {
    ABS {
        public UnaryOperation getOperationObject() { return new Abs(); }
    }, FACT {
        public UnaryOperation getOperationObject() { return new Fact(); }
    };

    public abstract UnaryOperation getOperationObject();
}
