package com.ebibli.service;

import com.ebibli.domain.BiblioClients;
import com.ebibli.dto.LivreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreService {

    private final BiblioClients biblioClients;

    public LivreService(BiblioClients biblioClients) {
        this.biblioClients = biblioClients;
    }

    public List<LivreDto> getAllEmpruntsByUtilisateur(Integer utilisateurId) {
        return biblioClients.getAllEmpruntsByUtilisateur(utilisateurId);
    }
}
