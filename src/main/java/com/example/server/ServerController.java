package com.example.server;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class ServerController {
    public static int requestID = 1;
    protected final Logger requestLogger = LoggerFactory.getLogger("request-logger");
    public ServerController() {

    }
    protected void debugRequestLogger(Instant start) {
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        requestLogger.debug("request #{} duration: {}ms", requestID, timeElapsed.toMillis());
        requestID++;
    }

    protected static String intArrayToStr(int[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int num : arr) {
            stringBuilder.append(num).append(',');
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
