package com.example.ex3;

import com.example.ex3.CalculatorModel.CalculateBinary;
import com.example.ex3.CalculatorModel.CalculateUnary;
import com.example.ex3.CalculatorModel.interfaces.BinaryOperation;
import com.example.ex3.CalculatorModel.interfaces.UnaryOperation;
import com.example.ex3.json.IndependentCalculatorJson;
import com.example.ex3.json.ResponseJson;

import java.util.Deque;

public class Calculator {
    public ResponseJson calculateIndependently(IndependentCalculatorJson json) {
        String errorMessage = "";
        String operation = json.getOperation();
        int[] arguments = json.getArguments();
        int result = -1;
        try {
            BinaryOperation binaryOperation = CalculateBinary.valueOf(operation.toUpperCase()).getOperationObject();
            if (arguments.length < 2) {
                errorMessage = "Error: Not enough arguments to perform the operation " + binaryOperation.getClass().getSimpleName();
            } else if (arguments.length > 2) {
                errorMessage = "Error: Too many arguments to perform the operation " + binaryOperation.getClass().getSimpleName();
            } else {
                ResponseJson binaryOperationResult = binaryOperation.operation(arguments[0], arguments[1]);
                if (!binaryOperationResult.getErrorMessage().isEmpty()) {
                    errorMessage = binaryOperationResult.getErrorMessage();
                } else {
                    result = binaryOperationResult.getResult();
                }
            }
        } catch (IllegalArgumentException e) {
            try {
                UnaryOperation unaryOperation = CalculateUnary.valueOf(operation.toUpperCase()).getOperationObject();
                if (arguments.length < 1) {
                    errorMessage = "Error: Not enough arguments to perform the operation " + unaryOperation.getClass().getSimpleName();
                } else if (arguments.length > 1) {
                    errorMessage = "Error: Too many arguments to perform the operation " + unaryOperation.getClass().getSimpleName();
                } else {
                    ResponseJson unaryOperationResult = unaryOperation.operation(arguments[0]);
                    if (!unaryOperationResult.getErrorMessage().isEmpty()) {
                        errorMessage = unaryOperationResult.getErrorMessage();
                    } else {
                        result = unaryOperationResult.getResult();
                    }
                }
            } catch (IllegalArgumentException exc) {
                errorMessage = "Error: unknown operation: " + operation;
            }
        }
        if (!errorMessage.isEmpty()) {
            System.out.println(errorMessage);
        }
        return new ResponseJson(result, errorMessage);
    }
    public ResponseJson calculateUsingStack(Deque<Integer> stack, String operation) {
        String errorMessage = "";
        int result = -1;
        try {
            BinaryOperation binaryOperation = CalculateBinary.valueOf(operation.toUpperCase()).getOperationObject();
            if (stack.size() < 2) {
                errorMessage = "Error: cannot implement operation " +
                        operation +
                        ". It requires 2 arguments and the stack has only " +
                        stack.size() + " arguments";

            } else {
                int x = stack.pop();
                int y = stack.pop();
                ResponseJson binaryOperationResult = binaryOperation.operation(x, y);
                if (!binaryOperationResult.getErrorMessage().isEmpty()) {
                    errorMessage = binaryOperationResult.getErrorMessage();
                } else {
                    result = binaryOperationResult.getResult();
                }
            }
        } catch (IllegalArgumentException e) {
            try {
                UnaryOperation unaryOperation = CalculateUnary.valueOf(operation.toUpperCase()).getOperationObject();
                if (stack.size() < 1) {
                    errorMessage = "Error: cannot implement operation " +
                            operation +
                            ". It requires 1 arguments and the stack has only 0 arguments";
                } else {
                    int x = stack.pop();
                    ResponseJson unaryOperationResult = unaryOperation.operation(x);
                    if (!unaryOperationResult.getErrorMessage().isEmpty()) {
                        errorMessage = unaryOperationResult.getErrorMessage();
                    } else {
                        result = unaryOperationResult.getResult();
                    }
                }
            } catch (IllegalArgumentException exc) {
                errorMessage = "Error: unknown operation: " + operation;
            }
        }
        if (!errorMessage.isEmpty()) {
            System.out.println(errorMessage);
        } else {
            System.out.println(result);
        }
        return new ResponseJson(result, errorMessage);
    }
}
