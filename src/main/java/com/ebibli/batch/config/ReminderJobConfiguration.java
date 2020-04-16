package com.ebibli.batch.config;

import com.ebibli.batch.listener.ReminderJobExecutionListener;
import com.ebibli.batch.processor.ReminderJobProcessor;
import com.ebibli.batch.reader.ReminderJobReader;
import com.ebibli.batch.writer.ReminderJobExecutionWriter;
import com.ebibli.batch.writer.ReminderJobWriter;
import com.ebibli.dto.LivreDto;
import com.ebibli.dto.UtilisateurDto;
import com.ebibli.service.LivreService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class ReminderJobConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private ReminderJobProperties reminderJobProperties;
    @Autowired
    private EmailConfiguration emailConfiguration;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private LivreService livreService;

    private List<UtilisateurDto> emprunteursRelances = new ArrayList<>();


    @Bean
    public Session getSession() {
        return new SessionConfiguration(emailConfiguration).configure();
    }

    @Bean
    @JobScope
    public ListItemReader<LivreDto> itemReader() {
        return new ReminderJobReader(livreService.getAllLivresEnRetard());
    }

    @StepScope
    @Bean
    public ItemProcessor<LivreDto, MimeMessage> processor(LivreService livreService) {
        return new ReminderJobProcessor(livreService, getSession());
    }

    @Bean
    @StepScope
    public ReminderJobWriter itemWriter() {
        return new ReminderJobWriter(emailConfiguration.javaMailSender());
    }

    @Bean
    public Job reminderJob(JobBuilderFactory jobs, Step firstStep) {
        return jobs.get("reminderJob")
                .incrementer(new RunIdIncrementer())
                .flow(firstStep)
                .end()
                .listener(reminderJobExecutionListener())
                .build();
    }

    /**
     * définition du workflow du job
     * @param stepBuilderFactory
     * @param itemWriter
     * @param itemProcessor
     */
    @Bean
    public Step firstStep(
            StepBuilderFactory stepBuilderFactory,
            ReminderJobWriter itemWriter,
            ItemProcessor<LivreDto, MimeMessage> itemProcessor) {
        return stepBuilderFactory.get("step1")
                .<LivreDto, MimeMessage>chunk(reminderJobProperties.getChunkSize())
                .reader(itemReader())
                .processor(itemProcessor)
                .writer(itemWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JobExecutionListener reminderJobExecutionListener() {
        return new ReminderJobExecutionListener(jobExecutionFileWriter());
    }

    @Bean
    public ReminderJobExecutionWriter jobExecutionFileWriter() {
        return new ReminderJobExecutionWriter(reminderJobProperties).initialize();
    }

    @Scheduled(cron = "${batch.cron.value}")
    public void perform() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("reminderJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }}

