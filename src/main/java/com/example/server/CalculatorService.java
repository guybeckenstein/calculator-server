package com.example.server;

import com.example.server.calculatorService.CalculateBinary;
import com.example.server.calculatorService.CalculateUnary;
import com.example.server.calculatorService.interfaces.BinaryOperation;
import com.example.server.calculatorService.interfaces.UnaryOperation;
import com.example.server.json.IndependentCalculator;
import com.example.server.json.Response;

import java.util.Deque;

public class CalculatorService {
    public Response calculateIndependently(IndependentCalculator json) {
        String errorMessage = "";
        String operation = json.operation();
        int[] arguments = json.arguments();
        int result = -1;
        try {
            BinaryOperation binaryOperation = CalculateBinary.valueOf(operation.toUpperCase()).getOperationObject();
            if (arguments.length < 2) {
                errorMessage = "Error: Not enough arguments to perform the operation " + binaryOperation.getClass().getSimpleName();
            } else if (arguments.length > 2) {
                errorMessage = "Error: Too many arguments to perform the operation " + binaryOperation.getClass().getSimpleName();
            } else {
                System.out.print(binaryOperation.getClass().getSimpleName() + ": ");
                Response binaryOperationResult = binaryOperation.operation(arguments[0], arguments[1]);
                if (!binaryOperationResult.errorMessage().isEmpty()) {
                    errorMessage = binaryOperationResult.errorMessage();
                } else {
                    result = binaryOperationResult.result();
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
                    System.out.print(unaryOperation.getClass().getSimpleName() + ": ");
                    Response unaryOperationResult = unaryOperation.operation(arguments[0]);
                    if (!unaryOperationResult.errorMessage().isEmpty()) {
                        errorMessage = unaryOperationResult.errorMessage();
                    } else {
                        result = unaryOperationResult.result();
                    }
                }
            } catch (IllegalArgumentException exc) {
                errorMessage = "Error: unknown operation: " + operation;
            }
        }
        return new Response(result, errorMessage);
    }
    public Response calculateUsingStack(Deque<Integer> stack, String operation) {
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
                System.out.print(binaryOperation.getClass().getSimpleName() + ": ");
                Response binaryOperationResult = binaryOperation.operation(x, y);
                if (!binaryOperationResult.errorMessage().isEmpty()) {
                    errorMessage = binaryOperationResult.errorMessage();
                } else {
                    result = binaryOperationResult.result();
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
                    System.out.print(unaryOperation.getClass().getSimpleName() + ": ");
                    Response unaryOperationResult = unaryOperation.operation(x);
                    if (!unaryOperationResult.errorMessage().isEmpty()) {
                        errorMessage = unaryOperationResult.errorMessage();
                    } else {
                        result = unaryOperationResult.result();
                    }
                }
            } catch (IllegalArgumentException exc) {
                errorMessage = "Error: unknown operation: " + operation;
            }
        }
        return new Response(result, errorMessage);
    }
}
