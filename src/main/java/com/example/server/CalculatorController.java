package com.example.server;

import com.example.server.json.ArgumentsJson;
import com.example.server.json.IndependentCalculator;
import com.example.server.json.Response;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

@RestController
public class CalculatorController extends ServerController {
    private final CalculatorService calculatorService = new CalculatorService();
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Logger independentLogger = LoggerFactory.getLogger("independent-logger");
    private final Logger stackLogger = LoggerFactory.getLogger("stack-logger");

    public CalculatorController() {
        super();
    }

    @PostMapping("/independent/calculate")
    public ResponseEntity<Response> independentCalculation(@RequestBody String body) {
        Instant start = Instant.now();
        requestLogger.info("Incoming request | #{} | resource: /independent/calculate | HTTP Verb POST | request #{}", requestID, requestID);
        try {
            IndependentCalculator calculator = new ObjectMapper().readValue(body, IndependentCalculator.class);
            Response calculate = calculatorService.calculateIndependently(calculator);
            if (!calculate.errorMessage().isEmpty()) {
                independentLogger.error("Server encountered an error ! message: error-message: {} | request #{}", calculate.errorMessage(), requestID);
                debugRequestLogger(start);
                return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
            }
            independentLogger.info("Performing operation {}. Result is {} | request #{}", calculator.operation(), calculate.result(), requestID);
            independentLogger.debug("Performing operation: {}({}) = {} | request #{}", calculator.operation(), calculator.arguments(), calculate.result(), requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } catch (IOException e) {
            independentLogger.error(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage() + " | request #{}", requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()),
                    HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/size")
    public ResponseEntity<Response> getStackSize() {
        Instant start = Instant.now();
        requestLogger.info("Incoming request | #{} | resource: /stack/size | HTTP Verb GET | request #{}", requestID, requestID);
        stackLogger.info("Stack size is {} | request #{}", stack.size(), requestID);
        stackLogger.debug("Stack content (first == top): [{}] | request #{}", stack, requestID);
        debugRequestLogger(start);
        return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
    }

    @PutMapping("/stack/arguments")
    public ResponseEntity<Response> addArguments(@RequestBody String body) {
        int startingStackSize = stack.size();
        Instant start = Instant.now();
        requestLogger.info("Incoming request | #{} | resource: /stack/arguments | HTTP Verb PUT | request #{}", requestID, requestID);
        try {
            int[] arguments = new ObjectMapper().readValue(body, ArgumentsJson.class).arguments();
            for (int argument : arguments) {
                stack.addFirst(argument);
            }
            stackLogger.info("Adding total of {} argument(s) to the stack | Stack size: {} | request #{}", arguments.length, stack.size(), requestID);
            stackLogger.debug("Adding arguments: {} | Stack size before {} | stack size after {} | request #{}", arguments, startingStackSize, stack.size(), requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            stackLogger.error(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage() + " | request #{}", requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(
                    new Response(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/operate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response> performOperation(@RequestParam String operation) {
        Instant start = Instant.now();

        requestLogger.info("Incoming request | #{} | resource: /stack/operate | HTTP Verb GET | request #{}", requestID, requestID);
        if (operation.matches("[a-zA-Z]+")) {
            List<Integer> arguments = getArgumentsFromStack(calculatorService.isBinaryOperation(operation));
            Response calculate = calculatorService.calculateUsingStack(stack, operation);
            stackLogger.info("Performing operation {}. Result is {} | stack size: {} | request #{}", operation, calculate.result(), stack.size(), requestID);
            if (!calculate.errorMessage().isEmpty()) {
                stackLogger.error("Server encountered an error ! message: {} | request #{}", calculate.errorMessage(), requestID);
                debugRequestLogger(start);
                return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
            }
            stackLogger.debug("Performing operation: {}({}) = {} | request #{}", operation, arguments, calculate.result(), requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } else {
            stackLogger.error("FormatException: operation cannot contain non-alphabet letters | request #{}", requestID);
            stackLogger.info("Performing operation {}. Result is -1 | stack size: {} | request #{}", operation, stack.size(), requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(
                    new Response(-1, "FormatException: operation cannot contain non-alphabet letters"),
                    HttpStatus.CONFLICT);
        }
    }

    private List<Integer> getArgumentsFromStack(boolean binaryOperation) {
        int argumentsInOperation = binaryOperation ? 2 : 1;
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < argumentsInOperation; i++)
            res.add(stack.pop());
        for (int i = argumentsInOperation - 1; i >= 0; i--)
            stack.add(res.get(i));

        return res;
    }

    @DeleteMapping("/stack/arguments")
    public ResponseEntity<Response> removeStackArguments(@RequestParam String count) {
        Instant start = Instant.now();
        requestLogger.info("Incoming request | #{} | resource: /stack/arguments | HTTP Verb DELETE | request #{}", requestID, requestID);
        try {
            int totalArgumentsToRemove = Integer.parseInt(count);
            if (stack.size() < totalArgumentsToRemove || totalArgumentsToRemove < 1) {
                stackLogger.error("Server encountered an error ! message: Error: cannot remove '{}' arguments from the stack. | request #{}", totalArgumentsToRemove, requestID);
                debugRequestLogger(start);
                return new ResponseEntity<>(
                        new Response(-1, "Error: cannot remove " + totalArgumentsToRemove
                                + "arguments  from the stack. It has only " + stack.size() + " arguments"), HttpStatus.CONFLICT);
            }
            IntStream.range(0, totalArgumentsToRemove).forEach((i) -> stack.pop());
            stackLogger.info("Removing total {} argument(s) from the stack | Stack size: {} | request #{}", totalArgumentsToRemove, stack.size(), requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
        } catch (NumberFormatException e) {
            String msg = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage() + " -> operation cannot contain non-numeric letters";
            stackLogger.error(msg + " | request #{}", requestID);
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(-1, msg), HttpStatus.CONFLICT);
        }
    }
}
