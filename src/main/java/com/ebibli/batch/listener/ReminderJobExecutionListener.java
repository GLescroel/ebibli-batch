package com.ebibli.batch.listener;

import com.ebibli.batch.writer.ReminderJobExecutionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

import static java.util.Collections.singletonList;

public class ReminderJobExecutionListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobExecutionListener.class);
    private final ReminderJobExecutionWriter itemWriter;

    public ReminderJobExecutionListener(ReminderJobExecutionWriter itemWriter) {
        this.itemWriter = itemWriter;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.debug("ReminderJobExecutionListener - beforeJob {}", getStepExecutionSummary(jobExecution));
        itemWriter.writeHeader();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.debug("ReminderJobExecutionListener - afterJob {}", getStepExecutionSummary(jobExecution));
        try {
            itemWriter.write(singletonList(jobExecution));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            itemWriter.close();
        }
    }

    private static String getStepExecutionSummary(JobExecution jobExecution) {
        return jobExecution.getStepExecutions().stream().findFirst().map(StepExecution::getSummary).orElse(null);
    }
}
