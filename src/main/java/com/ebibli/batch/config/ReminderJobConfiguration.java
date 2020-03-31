package com.ebibli.batch.config;

import com.ebibli.batch.processor.ReminderJobProcessor;
import com.ebibli.batch.reader.ReminderJobReader;
import com.ebibli.batch.writer.ReminderJobWriter;
import com.ebibli.dto.UtilisateurDto;
import com.ebibli.service.UtilisateurService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class ReminderJobConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private ReminderJobProperties reminderJobProperties;

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private UtilisateurService utilisateurService;


    @Bean
    @JobScope
    public ListItemReader<UtilisateurDto> itemReader() {
        return new ReminderJobReader(utilisateurService.getAllUtilisateur());
    }

    @StepScope
    @Bean
    public ItemProcessor<UtilisateurDto, UtilisateurDto> processor() {
        return new ReminderJobProcessor();
    }

    @Bean
    @StepScope
    public ReminderJobWriter itemWriter() {
        return new ReminderJobWriter();
    }

    @Bean
    public Job applicationJob(JobBuilderFactory jobs, Step firstStep) {
        return jobs.get("reminderJob")
                .incrementer(new RunIdIncrementer())
                .flow(firstStep)
                .end()
                //.listener(applicationJobExecutionListener())
                .build();
    }

    @Bean
    public Step firstStep(
            StepBuilderFactory stepBuilderFactory,
            ReminderJobWriter itemWriter,
            ItemProcessor<UtilisateurDto, UtilisateurDto> itemProcessor) {
        return stepBuilderFactory.get("step1")
                //.transactionManager(jpaTransactionManager(datasource))
                .<UtilisateurDto, UtilisateurDto>chunk(reminderJobProperties.getChunkSize())
                .faultTolerant()
                .skip(ValidationException.class)
                .skipLimit(reminderJobProperties.getSkipLimit())
                .reader(itemReader())
                .processor(itemProcessor)
                .writer(itemWriter)
                //.listener(new ApplicationProcessListener(applicationProperties.getOutputPath()))
                .build();
    }

    @Bean
    public JobExecution launch(JobBuilderFactory jobs, Step firstStep) throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException {
        return jobLauncher.run(applicationJob(jobs, firstStep), new JobParametersBuilder().toJobParameters());
    }
}

