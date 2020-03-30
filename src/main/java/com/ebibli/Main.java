package com.ebibli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new Main().runAsJavaApplication(args);
    }

    private void runAsJavaApplication(String[] args) {
        SpringApplicationBuilder application = new SpringApplicationBuilder();
        application.sources(Main.class);
        application.run(args);
        LOGGER.info("L'application a démarré...");
    }
}
