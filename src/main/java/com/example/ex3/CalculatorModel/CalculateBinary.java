package com.example.ex3.CalculatorModel;

import com.example.ex3.CalculatorModel.interfaces.BinaryOperation;

public enum CalculateBinary {
    DIVIDE {
        public BinaryOperation getOperationObject() { return new Divide(); }
    }, MINUS {
        public BinaryOperation getOperationObject() { return new Minus(); }
    }, PLUS {
        public BinaryOperation getOperationObject() { return new Plus(); }
    }, POW {
        public BinaryOperation getOperationObject() { return new Pow(); }
    }, TIMES {
        public BinaryOperation getOperationObject() { return new Times(); }
    };

    public abstract BinaryOperation getOperationObject();
}
