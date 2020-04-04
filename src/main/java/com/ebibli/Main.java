package com.ebibli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        runMyApp();
    }

    private static void runMyApp() {
        LOGGER.info("*********** Démarrage du batch ***********");
        SpringApplication.run(Main.class);
        LOGGER.info("*********** Batch terminé ***********");
    }
}
