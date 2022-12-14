package com.example.server;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class LoggerController extends ServerController {

    public LoggerController() {
        super();
    }

    @GetMapping("/logs/level")

    public String getLoggerCurrentLevel(@RequestParam(name = "logger-name") String loggerName) {
        Instant start = Instant.now();
        requestLogger.info("Incoming request | #{} | resource: /logs/level | HTTP Verb GET | request #{}", requestID, requestID);
        if (loggerName.equals("request-logger") || loggerName.equals("independent-logger") || loggerName.equals("stack-logger"))
        {
            Logger logger = LoggerFactory.getLogger(loggerName);
            if (logger.isTraceEnabled()) {
                debugRequestLogger(start);
                return "TRACE";
            } else if (logger.isDebugEnabled()) {
                debugRequestLogger(start);
                return "DEBUG";
            } else if (logger.isInfoEnabled()) {
                debugRequestLogger(start);
                return "INFO";
            } else if (logger.isWarnEnabled()) {
                debugRequestLogger(start);
                return "WARNING";
            } else if (logger.isErrorEnabled()) {
                debugRequestLogger(start);
                return "ERROR";
            } else {
                debugRequestLogger(start);
                return "FATAL";
            }
        } else {
            requestLogger.error("Failed to get logger level: logger '" + loggerName +"' does not exist | request #{}", requestID);
            debugRequestLogger(start);
            return "Failed to get logger level: logger '" + loggerName +"' does not exist";
        }
    }

    @PutMapping("/logs/level")
    public String setLoggerCurrentLevel(@RequestParam(name = "logger-name") String loggerName,
                                        @RequestParam(name = "logger-level") String loggerLevel,
                                        @Nullable @RequestBody String body) {
        Instant start = Instant.now();
        requestLogger.info("Incoming request | #{} | resource: /logs/level | HTTP Verb PUT | request #{}", requestID, requestID);
        if (!loggerName.equals("request-logger") && !loggerName.equals("independent-logger") && !loggerName.equals("stack-logger")) {
            requestLogger.error("Failed to set a new log level: logger '" + loggerName + "' does not exist | request #{}", requestID);
            debugRequestLogger(start);
            return "Failed to set a new log level: logger '" + loggerName +"' does not exist";
        }
        if (!loggerLevel.equalsIgnoreCase("TRACE") && !loggerLevel.equalsIgnoreCase("DEBUG")
                && !loggerLevel.equalsIgnoreCase("INFO") && !loggerLevel.equalsIgnoreCase("WARNING")
                && !loggerLevel.equalsIgnoreCase("ERROR") && !loggerLevel.equalsIgnoreCase("FATAL")) {
            requestLogger.error("Failed to set a new log level: level '" + loggerLevel + "' does not exist | request #{}", requestID);
            debugRequestLogger(start);
            return "Failed to set a new log level: level '" + loggerLevel + "' does not exist";
        }
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
        return loggerLevel.toUpperCase();
    }
}
