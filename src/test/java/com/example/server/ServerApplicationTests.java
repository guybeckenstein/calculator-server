package com.example.server;

import com.example.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;


@SpringBootTest
class ServerApplicationTests {
    private RestClient client;

    @BeforeTestExecution
    void init() {
        client = new RestClient(8496);
    }

    @Test
    void contextLoads() {
    }

}
