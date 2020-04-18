package com.ebibli.service;

import com.ebibli.domain.EmpruntClient;
import com.ebibli.dto.EmpruntDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpruntService {

    private final EmpruntClient empruntClient;

    public EmpruntService(EmpruntClient empruntClient) {
        this.empruntClient = empruntClient;
    }

    public List<EmpruntDto> getAllEmpruntsEnCoursByUtilisateur(Integer utilisateurId) {
        return empruntClient.getAllEmpruntsEnCoursByUtilisateur(utilisateurId);
    }

    public List<EmpruntDto> getAllLivresEnRetard() {
        return empruntClient.getAllEmpruntsEnRetard();
    }
}
