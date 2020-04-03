package com.ebibli.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "batch.reminder", ignoreUnknownFields = false)
public class ReminderJobProperties {

    @Value("${batch.reminder.chunk-size}")
    private int chunkSize;

    @Value("${batch.reminder.skip-limit}")
    private int skipLimit;

    @Value("${batch.reminder.reportPath}")
    private String reportPath;

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getSkipLimit() {
        return skipLimit;
    }

    public void setSkipLimit(int skipLimit) {
        this.skipLimit = skipLimit;
    }

    public String getReportPath() { return reportPath; }

    public void setReportPath(String reportFile) { this.reportPath = reportPath; }
}
