package com.oop.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LogController {

    @GetMapping("/logs")
    public String getLogs() {
        try {
            Path logFilePath = Paths.get("logs/application.log"); // Path to your log file
            List<String> logLines = Files.readAllLines(logFilePath); // Read all lines of the log file

            // Process the log lines to extract only the timestamp and message
            List<String> processedLogs = logLines.stream()
                    .map(this::processLogLine) // Process each line
                    .filter(line -> line != null) // Filter out null lines (if any)
                    .collect(Collectors.toList());

            // Join the processed log lines into a single string and return
            return String.join("\n", processedLogs);
        } catch (Exception e) {
            return "Failed to read logs: " + e.getMessage();
        }
    }

    // Method to extract the timestamp and log message from a log line
    private String processLogLine(String line) {
        // Regex to match timestamp and the message after it
        String regex = "^([\\d\\-T:.+\\d]+).*? : (.*)$"; // Matches the timestamp and message after " : "

        // If the line matches the regex, return only the timestamp and message
        if (line.matches(regex)) {
            return line.replaceAll(regex, "$1 $2"); // Replace with timestamp and message only
        }
        return null; // Return null for lines that do not match the desired format
    }
}
