package com.example.client;

import com.example.server.json.Arguments;
import com.example.server.json.IndependentCalculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

public class ClientApplication {
    /** Integration test **/
    public static void main(String[] args) {
        RestClient client = new RestClient(8496);
        try {
            System.out.println("### TEST 1 ###");
            testIndependentCalculationBinary(client);
            System.out.println(System.getProperty("line.separator") + System.getProperty("line.separator") + "### TEST 2 ###");
            testIndependentCalculationUnary(client);
            System.out.println(System.getProperty("line.separator") + System.getProperty("line.separator") + "### TEST 3 ###");
            testStackCalculations(client);
        } catch (ResourceAccessException e) {
            System.out.println("ERROR: Server is disconnected!");
        }
    }

    private static void testIndependentCalculationBinary(RestClient client) {
        // Testing binary operations
        try {
            client.testIndependentCalculation(new IndependentCalculator(new int[]{3, 1}, "multiply"));
        } catch (HttpClientErrorException e1) {
            System.out.println("Operation #1");
            System.out.println(e1.getLocalizedMessage() + System.getProperty("line.separator"));
            try {
                client.testIndependentCalculation(new IndependentCalculator(new int[]{3, 0}, "Divide"));
            } catch (HttpClientErrorException e2) {
                System.out.println("Operation #2");
                System.out.println(e2.getLocalizedMessage() + System.getProperty("line.separator"));
                try {
                    client.testIndependentCalculation(new IndependentCalculator(new int[]{3}, "PLUS"));
                } catch (HttpClientErrorException e3) {
                    System.out.println("Operation #3");
                    System.out.println(e3.getLocalizedMessage() + System.getProperty("line.separator"));
                    try {
                        client.testIndependentCalculation(new IndependentCalculator(new int[]{3, 0, 5}, "minus"));
                    } catch (HttpClientErrorException e4) {
                        System.out.println("Operation #4");
                        System.out.println(e4.getLocalizedMessage() + System.getProperty("line.separator"));

                        System.out.println("Operation #5");
                        ResponseEntity<String> result = client.testIndependentCalculation(new IndependentCalculator(new int[]{3, 5}, "minus"));
                        System.out.println(result + System.getProperty("line.separator"));
                    }
                }
            }
        }
    }
    private static void testIndependentCalculationUnary(RestClient client) {
        // Testing unary operations
        try {
            client.testIndependentCalculation(new IndependentCalculator(new int[]{-1}, "fact"));
        } catch (HttpClientErrorException e1) {
            System.out.println("Operation #1");
            System.out.println(e1.getLocalizedMessage() + System.getProperty("line.separator"));
            try {
                client.testIndependentCalculation(new IndependentCalculator(new int[]{-1, 2}, "Abs"));
            } catch (HttpClientErrorException e2) {
                System.out.println("Operation #2");
                System.out.println(e2.getLocalizedMessage() + System.getProperty("line.separator"));
                try {
                    client.testIndependentCalculation(new IndependentCalculator(new int[]{}, "FaCT"));
                } catch (HttpClientErrorException e3) {
                    System.out.println("Operation #3");
                    System.out.println(e3.getLocalizedMessage() + System.getProperty("line.separator"));

                    System.out.println("Operation #4");
                    ResponseEntity<String> result = client.testIndependentCalculation(new IndependentCalculator(new int[]{-1}, "Abs"));
                    System.out.println(result + System.getProperty("line.separator"));
                }
            }
        }
    }
    private static void testStackCalculations(RestClient client) {
        System.out.println("Operation #1");
        System.out.println(client.testGetStackSize() + System.getProperty("line.separator"));
        System.out.println("Operation #2");
        System.out.println(client.testAddArguments(new Arguments(new int[]{3, 4})) + System.getProperty("line.separator"));
        System.out.println("Operation #3");
        System.out.println(client.testGetStackSize() + System.getProperty("line.separator"));
        System.out.println("Operation #4");
        System.out.println(client.testRemoveStackArguments(1) + System.getProperty("line.separator"));
        try {
            client.testRemoveStackArguments(10);
        } catch (HttpClientErrorException e5) {
            System.out.println("Operation #5");
            System.out.println(e5.getLocalizedMessage() + System.getProperty("line.separator"));
            try {
                client.testPerformOperation("multiply");
            } catch (HttpClientErrorException e6) {
                System.out.println("Operation #6");
                System.out.println(e6.getLocalizedMessage() + System.getProperty("line.separator"));
                try {
                    client.testPerformOperation("plus");
                } catch (HttpClientErrorException e7) {
                    System.out.println("Operation #7");
                    System.out.println(e7.getLocalizedMessage() + System.getProperty("line.separator"));

                    System.out.println("Operation #8");
                    System.out.println(client.testPerformOperation("fact") + System.getProperty("line.separator"));

                    System.out.println("Operation #9");
                    System.out.println(client.testGetStackSize() + System.getProperty("line.separator"));
                }
            }
        }
    }
}
