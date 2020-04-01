package com.ebibli.batch.writer;

import com.ebibli.domain.Emprunteur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ReminderJobWriter implements ItemWriter<Emprunteur> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobWriter.class);

    @Override
    public void write(List<? extends Emprunteur> list) throws Exception {
        LOGGER.info("dans ReminderJobWriter");

        for (Emprunteur emprunteur : list) {
            LOGGER.info("Envoi email à " + emprunteur.getEmail());
        }
    }
}