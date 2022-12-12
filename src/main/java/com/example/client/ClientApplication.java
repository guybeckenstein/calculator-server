package com.example.client;

import com.example.server.json.ArgumentsJson;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

public class ClientApplication {
    /** Integration test **/
    public static void main(String[] args) {
        RestClient client = new RestClient(9583);
        try {
            System.out.println("### TEST 3 ###");
            testStackCalculations(client);
        } catch (ResourceAccessException e) {
            System.out.println("ERROR: Server is disconnected!");
        }
    }
    private static void testStackCalculations(RestClient client) {
        System.out.println("Operation #1");
        System.out.println(client.testGetStackSize() + System.getProperty("line.separator"));
        System.out.println("Operation #2");
        System.out.println(client.testAddArguments(new ArgumentsJson(new int[]{3, 4})) + System.getProperty("line.separator"));
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
