package com.ebibli.batch.processor;

import com.ebibli.dto.UtilisateurDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ReminderJobProcessor implements ItemProcessor<UtilisateurDto, UtilisateurDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobProcessor.class);

    @Override
    public UtilisateurDto process(UtilisateurDto utilisateurDto) throws Exception {

        LOGGER.info("process : " + utilisateurDto.getEmail());

        return utilisateurDto;
    }
}