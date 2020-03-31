package com.ebibli.service;

import com.ebibli.domain.BiblioClients;
import com.ebibli.dto.UtilisateurDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    private final BiblioClients biblioClients;

    public UtilisateurService(BiblioClients biblioClients) {
        this.biblioClients = biblioClients;
    }


    public List<UtilisateurDto> getAllUtilisateur() {
        return biblioClients.getAllUtilisateurs();
    }

}
