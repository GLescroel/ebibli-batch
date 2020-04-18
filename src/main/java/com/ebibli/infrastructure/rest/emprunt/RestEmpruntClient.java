package com.ebibli.infrastructure.rest.emprunt;

import com.ebibli.domain.EmpruntClient;
import com.ebibli.dto.EmpruntDto;

import java.util.List;

public class RestEmpruntClient implements EmpruntClient {

    private final EmpruntClientApi empruntClientApi;

    public RestEmpruntClient(EmpruntClientApi empruntClientApi) {
        this.empruntClientApi = empruntClientApi;
    }

    @Override
    public List<EmpruntDto> getAllEmpruntsEnCoursByUtilisateur(Integer utilisateurId) {
        return empruntClientApi.findEmpruntsEnCoursByUtilisateur(utilisateurId);
    }

    @Override
    public List<EmpruntDto> getAllEmpruntsEnRetard() {
        return empruntClientApi.findEmpruntsEnRetard();
    }
}
