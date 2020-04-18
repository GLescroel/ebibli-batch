package com.ebibli.domain;

import com.ebibli.dto.EmpruntDto;

import java.util.List;

public interface EmpruntClient {

    List<EmpruntDto> getAllEmpruntsEnCoursByUtilisateur(Integer utilisateurId);

    List<EmpruntDto> getAllEmpruntsEnRetard();
}

