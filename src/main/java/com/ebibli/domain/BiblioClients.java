package com.ebibli.domain;

import com.ebibli.dto.LivreDto;
import com.ebibli.dto.UtilisateurDto;

import java.util.List;

public interface BiblioClients {

    List<UtilisateurDto> getAllUtilisateurs();

    List<LivreDto> getAllEmpruntsByUtilisateur(Integer utilisateurId);
}
