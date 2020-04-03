package com.ebibli.batch.config;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SessionConfiguration {

    private final EmailConfiguration emailConfiguration;

    public SessionConfiguration(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }

    /**
     * Configuration de la Session attendue par les MimeMessage
     */
    public Session configure() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", emailConfiguration.getAuth());
        props.put("mail.smtp.starttls.enable", emailConfiguration.getStarttls());
        props.put("mail.smtp.host", emailConfiguration.getHost());
        props.put("mail.smtp.port", emailConfiguration.getPort());

        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfiguration.getUsername(), emailConfiguration.getPassword());
            }
        });
    }
}
