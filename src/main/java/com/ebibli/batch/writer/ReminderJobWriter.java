package com.ebibli.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.mail.javamail.MimeMessageItemWriter;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

public class ReminderJobWriter extends MimeMessageItemWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobWriter.class);

    private final JavaMailSender sender;

    public ReminderJobWriter(JavaMailSender sender) {
        this.sender = sender;
    }

    public void write(List<? extends MimeMessage> list) {
        LOGGER.info("dans ReminderJobWriter");

        for (MimeMessage message : list) {
            try {
                LOGGER.info("Envoi email à " + message.getRecipients(Message.RecipientType.TO).toString());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            sender.send(message);
        }
    }
}