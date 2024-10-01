package com.example.basicback.disasternews.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class DisasterNewsTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DisasterNewsTasklet.class);

    @Value("${python.script.path}")
    private String pythonScriptPath;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Starting DisasterNewsTasklet execution");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Python script executed successfully");
            } else {
                logger.error("Python script execution failed with exit code: " + exitCode);
                throw new RuntimeException("Python script execution failed");
            }
        } catch (Exception e) {
            logger.error("Error executing Python script", e);
            throw e;
        }

        logger.info("DisasterNewsTasklet execution completed");
        return RepeatStatus.FINISHED;
    }
}