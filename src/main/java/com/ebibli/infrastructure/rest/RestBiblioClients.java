package com.ebibli.infrastructure.rest;

import com.ebibli.domain.BiblioClients;
import com.ebibli.dto.UtilisateurDto;

import java.util.List;

public class RestBiblioClients implements BiblioClients {

    private final BiblioClientApi biblioClientApi;

    public RestBiblioClients(BiblioClientApi biblioClientApi) {
        this.biblioClientApi = biblioClientApi;
    }

    @Override
    public List<UtilisateurDto> getAllUtilisateurs() {
        return biblioClientApi.getAllUtilisateurs();
    }
}
