package com.example.server;

import com.example.server.json.ArgumentsJson;
import com.example.server.json.IndependentCalculator;
import com.example.server.json.Response;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

@RestController
public class CalculatorController {
    private final CalculatorService calculatorService = new CalculatorService();
    private final Deque<Integer> stack = new ArrayDeque<>();

    @PostMapping("/independent/calculate")
    public ResponseEntity<Response> independentCalculation(@RequestBody String body) {
        try {
            Response calculate = calculatorService.calculateIndependently(
                    new ObjectMapper().readValue(body, IndependentCalculator.class)
            );
        if (!calculate.errorMessage().isEmpty()) {
            System.out.println(calculate.errorMessage());
            return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
        }
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            return new ResponseEntity<>(new Response(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()),
                    HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/size")
    public ResponseEntity<Response> getStackSize() {
        System.out.println("Returning stack size: " + stack.size());
        return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
    }

    @PutMapping("/stack/arguments")
    public ResponseEntity<Response> addArguments(@RequestBody String body) {
        System.out.println("Stack before adding arguments: " + stack);
        try {
            int[] arguments = new ObjectMapper().readValue(body, ArgumentsJson.class).arguments();
            for (int argument : arguments) {
                stack.addFirst(argument);
            }
            System.out.println("Stack after adding arguments: " + stack);
            return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            return new ResponseEntity<>(
                    new Response(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/operate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response> performOperation(@RequestParam String operation) {
        if (operation.matches("[a-zA-Z]+")) {
            Response calculate = calculatorService.calculateUsingStack(stack, operation);
            if (!calculate.errorMessage().isEmpty()) {
                System.out.println(calculate.errorMessage());
                return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } else {
            System.out.println("FormatException: operation cannot contain non-alphabet letters");
            return new ResponseEntity<>(
                    new Response(-1, "FormatException: operation cannot contain non-alphabet letters"),
                    HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/stack/arguments")
    public ResponseEntity<Response> removeStackArguments(@RequestParam String count) {
        System.out.println("Stack before removing arguments: " + stack);
        try {
            int totalArgumentsToRemove = Integer.parseInt(count);
            if (stack.size() < totalArgumentsToRemove) {
                return new ResponseEntity<>(
                        new Response(-1, "Error: cannot remove " + totalArgumentsToRemove
                                + "arguments  from the stack. It has only " + stack.size() + " arguments"), HttpStatus.CONFLICT);
            } else if (totalArgumentsToRemove < 1) {
                return new ResponseEntity<>(
                        new Response(-1, "Error: cannot remove '" + totalArgumentsToRemove
                                + "' arguments from the stack."), HttpStatus.CONFLICT);
            }
            IntStream.range(0, totalArgumentsToRemove).forEach((i) -> stack.pop());
            System.out.println("Stack after removing arguments: " + stack);
            return new ResponseEntity<>(new Response(stack.size(), ""), HttpStatus.OK);
        } catch (NumberFormatException e) {
            String msg = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage() + " -> operation cannot contain non-numeric letters";
            System.out.println(msg);
            return new ResponseEntity<>(new Response(-1, msg), HttpStatus.CONFLICT);
        }
    }
}
