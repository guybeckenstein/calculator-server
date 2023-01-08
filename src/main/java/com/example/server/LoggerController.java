package com.example.server;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class LoggerController extends ServerController {

    public LoggerController() {
        super();
    }

    @GetMapping("/logs/level")

    public ResponseEntity<String> getLoggerCurrentLevel(@RequestParam(name = "logger-name") String loggerName) {
        Instant start = Instant.now();
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /logs/level | HTTP Verb GET", requestID);
        if (loggerName.equals("request-logger") || loggerName.equals("independent-logger") || loggerName.equals("stack-logger"))
        {
            String responseString;
            Logger logger = LoggerFactory.getLogger(loggerName);
            if (logger.isTraceEnabled()) {
                responseString = "TRACE";
            } else if (logger.isDebugEnabled()) {
                responseString = "DEBUG";
            } else if (logger.isInfoEnabled()) {
                responseString = "INFO";
            } else if (logger.isWarnEnabled()) {
                responseString = "WARNING";
            } else if (logger.isErrorEnabled()) {
                responseString = "ERROR";
            } else {
                responseString = "FATAL";
            }
            debugRequestLogger(start);
            return new ResponseEntity<>("Success: " + responseString, HttpStatus.OK);
        } else {
            requestLogger.error("Failure: Failed to get logger level: logger '" + loggerName +"' does not exist");
            debugRequestLogger(start);
            return new ResponseEntity<>("Failure: Failed to get logger level: logger '" + loggerName +"' does not exist", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/logs/level")
    public ResponseEntity<String> setLoggerCurrentLevel(@RequestParam(name = "logger-name") String loggerName,
                                        @RequestParam(name = "logger-level") String loggerLevel,
                                        @Nullable @RequestBody String body) {
        Instant start = Instant.now();
        ThreadContext.put("requestID", Integer.toString(requestID));
        requestLogger.info("Incoming request | #{} | resource: /logs/level | HTTP Verb PUT", requestID);

        if (!loggerName.equals("request-logger") && !loggerName.equals("independent-logger") && !loggerName.equals("stack-logger")) {
            requestLogger.error("Failure: Failed to set a new log level: logger '" + loggerName + "' does not exist");
            debugRequestLogger(start);
            return new ResponseEntity<>("Failure: Failed to set a new log level: logger '" + loggerName +"' does not exist", HttpStatus.CONFLICT);
        }
        else if (!loggerLevel.equalsIgnoreCase("TRACE") && !loggerLevel.equalsIgnoreCase("DEBUG")
                && !loggerLevel.equalsIgnoreCase("INFO") && !loggerLevel.equalsIgnoreCase("WARNING")
                && !loggerLevel.equalsIgnoreCase("ERROR") && !loggerLevel.equalsIgnoreCase("FATAL")) {
            requestLogger.error("Failure: Failed to set a new log level: level '" + loggerLevel + "' does not exist");
            debugRequestLogger(start);
            return new ResponseEntity<>("Failure: Failed to set a new log level: level '" + loggerLevel + "' does not exist", HttpStatus.CONFLICT);
        }
        else {
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            Configuration configuration = loggerContext.getConfiguration();
            switch (loggerLevel.toUpperCase()) {
                case "TRACE" -> configuration.getLoggerConfig(loggerName).setLevel(Level.TRACE);
                case "DEBUG" -> configuration.getLoggerConfig(loggerName).setLevel(Level.DEBUG);
                case "INFO" -> configuration.getLoggerConfig(loggerName).setLevel(Level.INFO);
                case "WARNING" -> configuration.getLoggerConfig(loggerName).setLevel(Level.WARN);
                case "ERROR" -> configuration.getLoggerConfig(loggerName).setLevel(Level.ERROR);
                default -> configuration.getLoggerConfig(loggerName).setLevel(Level.FATAL);
            }

            loggerContext.updateLoggers(configuration);
            debugRequestLogger(start);
            return new ResponseEntity<>("Success: " + loggerLevel.toUpperCase(), HttpStatus.OK);
        }
    }
}
