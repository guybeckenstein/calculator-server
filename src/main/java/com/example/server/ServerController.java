package com.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class ServerController {
    public static int requestID = 1;
    protected final Logger requestLogger = LoggerFactory.getLogger("request-logger");
    public ServerController() {

    }
    protected void debugRequestLogger(Instant start) {
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        requestLogger.debug("request #{} duration: {}ms | request #{}", requestID, timeElapsed.toMillis(), requestID++);
    }
}
