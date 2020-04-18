package com.ebibli.batch.processor;

import com.ebibli.domain.Emprunteur;
import com.ebibli.dto.EmpruntDto;
import com.ebibli.dto.UtilisateurDto;
import com.ebibli.service.EmpruntService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

public class ReminderJobProcessor implements ItemProcessor<EmpruntDto, MimeMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderJobProcessor.class);

    private final EmpruntService empruntService;
    private final Session session;
    private List<UtilisateurDto> emprunteursRelances = new ArrayList<>();

    public ReminderJobProcessor(EmpruntService empruntService, Session session) {
        this.empruntService = empruntService;
        this.session = session;
    }

    /**
     * Identification des utilisateurs à relancer et création du message
     * @param empruntEnRetard
     * @return le MimeMessage à envoyer à l'utilisateur ayant des prêts en retard, null sinon
     * @throws Exception
     */
    @Override
    public MimeMessage process(EmpruntDto empruntEnRetard) throws Exception {

        for (UtilisateurDto emprunteur : emprunteursRelances) {
            if(emprunteur.getId() == empruntEnRetard.getEmprunteur().getId()) {
                return null;
            }
        }
        emprunteursRelances.add(empruntEnRetard.getEmprunteur());

        List<EmpruntDto> emprunts = empruntService.getAllEmpruntsEnCoursByUtilisateur(empruntEnRetard.getEmprunteur().getId());

        if (!emprunts.isEmpty()) {
            LOGGER.info(String.format("emprunteur : %s", empruntEnRetard.getEmprunteur().getEmail()));
            Emprunteur emprunteur = new Emprunteur();
            emprunteur.setEmail(empruntEnRetard.getEmprunteur().getEmail());
            emprunteur.setNom(empruntEnRetard.getEmprunteur().getNom());
            emprunteur.setPrenom(empruntEnRetard.getEmprunteur().getPrenom());

            for (EmpruntDto emprunt : emprunts) {
                if (emprunt.getEnRetard() == true) {
                    emprunteur.addEmpruntRetard(emprunt);
                } else {
                    emprunteur.addEmprunt(emprunt);
                }
            }

            if (!emprunteur.getEmpruntsRetard().isEmpty()) {

                String body = writeMessage(emprunteur);

                Emprunteur finalEmprunteur = emprunteur;
                MimeMessage message = new MimeMessage(session);
                MimeMessagePreparator preparator = mimeMessage -> {
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(finalEmprunteur.getEmail()));
                    message.setFrom(new InternetAddress("eBibli@oc.com"));
                    message.setSubject("eBibli : retard");
                    message.setText(body, "UTF-8", "html");
                };
                preparator.prepare(message);
                return message;
            }
        }
        return null;
    }

    /**
     * Rédaction du corps du message
     * @param emprunteur
     * @return le corps texte du message
     */
    private String writeMessage(Emprunteur emprunteur) {
        String text = String.format("Bonjour %s %s,\n\n", emprunteur.getPrenom(), emprunteur.getNom());
        text += "La date de retour des livres suivants est dépassée :\n";
        for (EmpruntDto emprunt : emprunteur.getEmpruntsRetard()) {
            text += String.format("- %s attendu le %s à la biliothèque : %s\n",
                    emprunt.getLivre().getOuvrage().getTitre(),
                    emprunt.getDateRetourPrevu().toString(),
                    emprunt.getLivre().getBibliotheque().getNom());
        }
        if (!emprunteur.getEmprunts().isEmpty()) {
            text += "Nous vous rappelons vos autres emprunts en cours :\n";
            for (EmpruntDto emprunt : emprunteur.getEmprunts()) {
                text += String.format("- %s attendu le %s à la biliothèque : %s",
                        emprunt.getLivre().getOuvrage().getTitre(),
                        emprunt.getDateRetourPrevu().toString(),
                        emprunt.getLivre().getBibliotheque().getNom());
            }
        }
        return text;
    }
}