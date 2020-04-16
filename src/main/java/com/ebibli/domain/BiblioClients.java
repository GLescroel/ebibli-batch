package com.ebibli.domain;

import com.ebibli.dto.LivreDto;

import java.util.List;

public interface BiblioClients {

    List<LivreDto> getAllEmpruntsByUtilisateur(Integer utilisateurId);

    List<LivreDto> getAllEmpruntsLate();
}
