package com.ebibli.batch.processor;

import com.ebibli.domain.Emprunteur;
import com.ebibli.dto.LivreDto;
import com.ebibli.dto.UtilisateurDto;
import com.ebibli.service.LivreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReminderJobProcessor implements ItemProcessor<UtilisateurDto, Emprunteur> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobProcessor.class);

    private final LivreService livreService;

    public ReminderJobProcessor(LivreService livreService) {
        this.livreService = livreService;
    }

    @Override
    public Emprunteur process(UtilisateurDto utilisateur) throws Exception {
        LOGGER.info("process : " + utilisateur.getEmail());

        List<LivreDto> emprunts = livreService.getAllEmpruntsByUtilisateur(utilisateur.getId());

        if (!emprunts.isEmpty()) {
            for (LivreDto emprunt : emprunts) {
                if (emprunt.getDateRetourPrevu().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(LocalDate.now())) {
                    LOGGER.info("emprunteur : " + utilisateur.getEmail());
                    return new Emprunteur().builder()
                            .email(utilisateur.getEmail())
                            .nom(utilisateur.getNom())
                            .prenom(utilisateur.getPrenom())
                            .emprunts(emprunts)
                            .build();
                }
            }
        }

        LOGGER.info("sans emprunt : " + utilisateur.getEmail());
        return null;
    }
}