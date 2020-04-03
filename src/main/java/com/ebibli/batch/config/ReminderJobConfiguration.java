package com.ebibli.batch.config;

import com.ebibli.batch.listener.ReminderJobExecutionListener;
import com.ebibli.batch.processor.ReminderJobProcessor;
import com.ebibli.batch.reader.ReminderJobReader;
import com.ebibli.batch.writer.ReminderJobWriter;
import com.ebibli.dto.UtilisateurDto;
import com.ebibli.service.LivreService;
import com.ebibli.service.UtilisateurService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private UtilisateurService utilisateurService;

    @Bean
    public Session getSession() {
        return new SessionConfiguration(emailConfiguration).configure();
    }

    @Bean
    @JobScope
    public ListItemReader<UtilisateurDto> itemReader() {
        return new ReminderJobReader(utilisateurService.getAllUtilisateur());
    }

    @StepScope
    @Bean
    public ItemProcessor<UtilisateurDto, MimeMessage> processor(LivreService livreService, Session session) {
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
            ItemProcessor<UtilisateurDto, MimeMessage> itemProcessor) {
        return stepBuilderFactory.get("step1")
                .<UtilisateurDto, MimeMessage>chunk(reminderJobProperties.getChunkSize())
                .faultTolerant()
                .skip(ValidationException.class)
                .skipLimit(reminderJobProperties.getSkipLimit())
                .reader(itemReader())
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public JobExecutionListener reminderJobExecutionListener() {
        return new ReminderJobExecutionListener(jobExecutionFileWriter());
    }

    @Bean
    public FlatFileItemWriter<JobExecution> jobExecutionFileWriter() {
        final FlatFileItemWriter<JobExecution> writer = new FlatFileItemWriter<>();
        Path path = Paths.get(reminderJobProperties.getReportPath(),
                String.format("reminder-job-execution-%s.txt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));
        writer.setResource(new FileSystemResource(path.toString()));
        writer.setHeaderCallback(headerWriter -> headerWriter.append("--- Rapport d'execution du Batch de relance ---"));
        writer.setLineAggregator(item -> new StringBuilder()
                .append("Statut: ")
                .append(item.getStatus()).append(System.lineSeparator())
                .append("Date de début: ")
                .append(item.getStartTime()).append(System.lineSeparator())
                .append("Date de fin: ")
                .append(item.getEndTime()).append(System.lineSeparator())
                .append("Nombre d'utilisateurs lus: ")
                .append(item.getStepExecutions().stream().findFirst().map(StepExecution::getReadCount).get()).append(System.lineSeparator())
                .append("Nombre d'utilisateurs filtrées: ")
                .append(item.getStepExecutions().stream().findFirst().map(StepExecution::getFilterCount).get()).append(System.lineSeparator())
                .append("Nombre d'utilisateurs rejetées: ")
                .append(item.getStepExecutions().stream().findFirst().map(StepExecution::getProcessSkipCount).get()).append(System.lineSeparator())
                .append("Nombre d'utilisateurs relancés: ")
                .append(item.getStepExecutions().stream().findFirst().map(StepExecution::getWriteCount).get()).append(System.lineSeparator())
                .append("Erreurs: ")
                .append(item.getStepExecutions().stream().findFirst().map(StepExecution::getFailureExceptions).get()).append(System.lineSeparator())
                .toString());
        writer.setAppendAllowed(true);
        ExecutionContext executionContext = new ExecutionContext();
        writer.open(executionContext);
        return writer;
    }

    @Bean
    public JobExecution launch(JobBuilderFactory jobs, Step firstStep) throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException {
        return jobLauncher.run(reminderJob(jobs, firstStep), new JobParametersBuilder().toJobParameters());
    }
}

