package com.example.ex3;

import com.example.ex3.json.ArgumentsJson;
import com.example.ex3.json.IndependentCalculatorJson;
import com.example.ex3.json.ResponseJson;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        ResponseJson calculate = calculator.calculateIndependently(new ObjectMapper().readValue(body, IndependentCalculatorJson.class));
        if (!calculate.getErrorMessage().isEmpty()) {
            return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(calculate, HttpStatus.OK);
    }

    @GetMapping("/stack/size")
    public ResponseEntity<ResponseJson> getStackSize() {
        System.out.println("Returning stack size: " + stack.size());
        return new ResponseEntity<>(new ResponseJson(stack.size(), ""), HttpStatus.OK);
    }

    @PutMapping("/stack/arguments")
    public ResponseEntity<ResponseJson> addArguments(@RequestBody String body) throws JsonProcessingException {
        System.out.println("Stack before adding arguments: " + stack);
        int[] arguments = new ObjectMapper().readValue(body, ArgumentsJson.class).getArguments();
        for (int argument : arguments) {
            stack.addFirst(argument);
        }
        System.out.println("Stack after adding arguments: " + stack);
        return new ResponseEntity<>(new ResponseJson(stack.size(), ""), HttpStatus.OK);
    }

    @GetMapping("/stack/operate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseJson> performOperation(@RequestParam String operation) {
        ResponseJson calculate = calculator.calculateUsingStack(stack, operation);
        if (!calculate.getErrorMessage().isEmpty()) {
            return new ResponseEntity<>(calculate, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(calculate, HttpStatus.OK);
    }

    @DeleteMapping("/stack/arguments")
    public ResponseEntity<ResponseJson> removeStackArguments(@RequestParam String count) {
        int totalArgumentsToRemove = Integer.parseInt(count);
        if (stack.size() < totalArgumentsToRemove) {
            return new ResponseEntity<>(
                    new ResponseJson(-1, "Error: cannot remove " + totalArgumentsToRemove
                            + " from the stack. It has only " + stack.size() + " arguments"), HttpStatus.CONFLICT);
        }
        IntStream.range(0, totalArgumentsToRemove).forEach((i) -> stack.pop());
        return new ResponseEntity<>(new ResponseJson(stack.size(), ""), HttpStatus.OK);
    }
}
