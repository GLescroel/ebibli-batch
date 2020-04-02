package com.ebibli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        runMyApp();
    }

    @Scheduled(cron = "${batch.cron.value}")
    private static void runMyApp() {
        final ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        SpringApplication.exit(context);
        LOGGER.info("*********** Batch terminé ***********");
    }
}
