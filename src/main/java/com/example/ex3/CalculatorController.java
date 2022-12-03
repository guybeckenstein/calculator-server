package com.example.ex3;

import com.example.ex3.json.ArgumentsJson;
import com.example.ex3.json.IndependentCalculatorJson;
import com.example.ex3.json.ResponseJson;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private final Calculator calculator = new Calculator();
    private final Deque<Integer> stack = new ArrayDeque<>();

    @PostMapping("/independent/calculate")
    public ResponseEntity<ResponseJson> independentCalculation(@RequestBody String body) throws IOException {
        try {
            ResponseJson calculate = calculator.calculateIndependently(
                    new ObjectMapper().readValue(body, IndependentCalculatorJson.class)
            );
        if (!calculate.getErrorMessage().isEmpty()) {
            System.out.println(calculate.getErrorMessage());
            return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
        }
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } catch (JsonMappingException | JsonParseException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            return new ResponseEntity<>(new ResponseJson(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()),
                    HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/size")
    public ResponseEntity<ResponseJson> getStackSize() {
        System.out.println("Returning stack size: " + stack.size());
        return new ResponseEntity<>(new ResponseJson(stack.size(), ""), HttpStatus.OK);
    }

    @PutMapping("/stack/arguments")
    public ResponseEntity<ResponseJson> addArguments(@RequestBody String body) throws JsonProcessingException {
        System.out.println("Stack before adding arguments: " + stack);
        try {
            int[] arguments = new ObjectMapper().readValue(body, ArgumentsJson.class).getArguments();
            for (int argument : arguments) {
                stack.addFirst(argument);
            }
            System.out.println("Stack after adding arguments: " + stack);
            return new ResponseEntity<>(new ResponseJson(stack.size(), ""), HttpStatus.OK);
        } catch (JsonMappingException | JsonParseException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            return new ResponseEntity<>(
                    new ResponseJson(-1, e.getClass().getSimpleName() + ": " + e.getLocalizedMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/stack/operate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseJson> performOperation(@RequestParam String operation) {
        if (operation.matches("[a-zA-Z]+")) {
            ResponseJson calculate = calculator.calculateUsingStack(stack, operation);
            if (!calculate.getErrorMessage().isEmpty()) {
                System.out.println(calculate.getErrorMessage());
                return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(calculate, HttpStatus.OK);
        } else {
            System.out.println("FormatException: operation cannot contain non-alphabet letters");
            return new ResponseEntity<>(
                    new ResponseJson(-1, "FormatException: operation cannot contain non-alphabet letters"),
                    HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/stack/arguments")
    public ResponseEntity<ResponseJson> removeStackArguments(@RequestParam String count) {
        System.out.println("Stack before removing arguments: " + stack);
        try {
            int totalArgumentsToRemove = Integer.parseInt(count);
            if (stack.size() < totalArgumentsToRemove) {
                return new ResponseEntity<>(
                        new ResponseJson(-1, "Error: cannot remove " + totalArgumentsToRemove
                                + " from the stack. It has only " + stack.size() + " arguments"), HttpStatus.CONFLICT);
            }
            IntStream.range(0, totalArgumentsToRemove).forEach((i) -> stack.pop());
            System.out.println("Stack after removing arguments: " + stack);
            return new ResponseEntity<>(new ResponseJson(stack.size(), ""), HttpStatus.OK);
        } catch (NumberFormatException e) {
            String msg = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage() + " -> operation cannot contain non-numeric letters";
            System.out.println(msg);
            return new ResponseEntity<>(new ResponseJson(-1, msg), HttpStatus.CONFLICT);
        }
    }
}
