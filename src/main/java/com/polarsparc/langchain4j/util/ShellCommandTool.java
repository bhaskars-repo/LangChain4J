/*
 * Name:   ShellCommandTool
 * Author: Bhaskar S
 * Date:   12/31/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.util;

import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Component
public class ShellCommandTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellCommandTool.class);

    @Tool("Execute shell commands on the system. Use this to to execute any system command, etc.")
    public String executeShellCommand(String command) {
        LOGGER.info("Executing shell command: {}", command);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return "Error: Command timed out after 30 seconds";
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                return "Error executing command. Exit code: " + exitCode + "\nOutput: " + output;
            }

            return output.toString().trim();
        } catch (Exception ex) {
            LOGGER.error("Error executing shell command", ex);
            return "Error: " + ex.getMessage();
        }
    }
}
