package com.example.basicback.disasternews.config;


import com.example.basicback.disasternews.job.DisasterNewsTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public Job disasterNewsJob(JobRepository jobRepository, Step disasterNewsStep) {
        return new JobBuilder("disasterNewsJob", jobRepository)
                .start(disasterNewsStep)
                .build();
    }

    @Bean
    public Step disasterNewsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DisasterNewsTasklet disasterNewsTasklet) {
        return new StepBuilder("disasterNewsStep", jobRepository)
                .tasklet(disasterNewsTasklet, transactionManager)
                .build();
    }
}

