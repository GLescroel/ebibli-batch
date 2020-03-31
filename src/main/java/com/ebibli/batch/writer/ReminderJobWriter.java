package com.ebibli.batch.writer;

import com.ebibli.dto.UtilisateurDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ReminderJobWriter implements ItemWriter<UtilisateurDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobWriter.class);

    @Override
    public void write(List<? extends UtilisateurDto> list) throws Exception {
        LOGGER.info("dans ReminderJobWriter");

        for (UtilisateurDto utilisateur : list) {
            LOGGER.info(utilisateur.getEmail());
        }
    }
}