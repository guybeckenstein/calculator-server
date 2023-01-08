package com.example.server;

import com.example.server.json.ArgumentsJson;
import com.example.server.json.IndependentCalculator;
import com.example.server.json.Response;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

@RestController
@Slf4j
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
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /independent/calculate | HTTP Verb POST", requestID);
        try {
            IndependentCalculator calculator = new ObjectMapper().readValue(body, IndependentCalculator.class);
            Response calculate = calculatorService.calculateIndependently(calculator);
            if (!calculate.errorMessage().isEmpty()) {
                independentLogger.error("Server encountered an error ! message: {}", calculate.errorMessage());
                debugRequestLogger(start);
                return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
            }
            independentLogger.info("Performing operation {}. Result is {}", calculator.operation(), calculate.result());
            independentLogger.debug("Performing operation: {}({}) = {}", calculator.operation(), intArrayToStr(calculator.arguments()), calculate.result());
            debugRequestLogger(start);
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } catch (IOException e) {
            independentLogger.error(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()),
                    HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/size")
    public ResponseEntity<Response> getStackSize() {
        Instant start = Instant.now();
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /stack/size | HTTP Verb GET", requestID);
        stackLogger.info("Stack size is {}", stack.size());
        stackLogger.debug("Stack content (first == top): {}", stack);
        debugRequestLogger(start);
        return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
    }

    @PutMapping("/stack/arguments")
    public ResponseEntity<Response> addArguments(@RequestBody String body) {
        int startingStackSize = stack.size();
        Instant start = Instant.now();
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /stack/arguments | HTTP Verb PUT", requestID);
        try {
            int[] arguments = new ObjectMapper().readValue(body, ArgumentsJson.class).arguments();
            for (int argument : arguments) {
                stack.addFirst(argument);
            }
            stackLogger.info("Adding total of {} argument(s) to the stack | Stack size: {}", arguments.length, stack.size());
            stackLogger.debug("Adding arguments: {} | Stack size before {} | stack size after {}", intArrayToStr(arguments), startingStackSize, stack.size());
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            stackLogger.error(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            debugRequestLogger(start);
            return new ResponseEntity<>(
                    new Response(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/operate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response> performOperation(@RequestParam String operation) {
        Instant start = Instant.now();
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /stack/operate | HTTP Verb GET", requestID);
        if (operation.matches("[a-zA-Z]+")) {
            List<Integer> arguments = getArgumentsFromStack(calculatorService.isBinaryOperation(operation));
            Response calculate = calculatorService.calculateUsingStack(stack, operation);
            if (!calculate.errorMessage().isEmpty()) {
                stackLogger.error("Server encountered an error ! message: {}", calculate.errorMessage());
                debugRequestLogger(start);
                return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
            }
            stackLogger.info("Performing operation {}. Result is {} | stack size: {}", operation, calculate.result(), stack.size());
            String debugArguments = intArrayToStr(Objects.requireNonNull(arguments).stream().mapToInt(Integer::intValue).toArray());
            stackLogger.debug("Performing operation: {}({}) = {}", operation, debugArguments, calculate.result());
            debugRequestLogger(start);
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } else {
            stackLogger.error("FormatException: operation cannot contain non-alphabet letters");
            stackLogger.info("Performing operation {}. Result is -1 | stack size: {}", operation, stack.size());
            debugRequestLogger(start);
            return new ResponseEntity<>(
                    new Response(-1, "FormatException: operation cannot contain non-alphabet letters"),
                    HttpStatus.CONFLICT);
        }
    }

    private List<Integer> getArgumentsFromStack(boolean binaryOperation) {
        int argumentsInOperation = binaryOperation ? 2 : 1;
        if (stack.size() < argumentsInOperation) {
            return null;
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < argumentsInOperation; i++) {
            res.add(stack.pop());
        }
        for (int i = argumentsInOperation - 1; i >= 0; i--) {
            stack.addFirst(res.get(i));
        }

        return res;
    }

    @DeleteMapping("/stack/arguments")
    public ResponseEntity<Response> removeStackArguments(@RequestParam String count) {
        Instant start = Instant.now();
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /stack/arguments | HTTP Verb DELETE", requestID);
        try {
            int totalArgumentsToRemove = Integer.parseInt(count);
            if (stack.size() < totalArgumentsToRemove || totalArgumentsToRemove < 1) {
                stackLogger.error("Server encountered an error ! message: Error: cannot remove '{}' arguments from the stack.", totalArgumentsToRemove);
                debugRequestLogger(start);
                return new ResponseEntity<>(
                        new Response(-1, "Error: cannot remove " + totalArgumentsToRemove
                                + "arguments  from the stack. It has only " + stack.size() + " arguments"), HttpStatus.CONFLICT);
            }
            IntStream.range(0, totalArgumentsToRemove).forEach((i) -> stack.pop());
            stackLogger.info("Removing total {} argument(s) from the stack | Stack size: {}", totalArgumentsToRemove, stack.size());
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
        } catch (NumberFormatException e) {
            String msg = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage() + " -> operation cannot contain non-numeric letters";
            stackLogger.error(msg);
            debugRequestLogger(start);
            return new ResponseEntity<>(new Response(-1, msg), HttpStatus.CONFLICT);
        }
    }
}
