package com.ebibli.infrastructure.rest;

import com.ebibli.dto.LivreDto;
import com.ebibli.dto.UtilisateurDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Accès aux API du backend avec Feign
 */
@FeignClient(name = "biblio-services",
        url = "${clients.com-ebibli-v1-vs.endpoint}")
public interface BiblioClientApi {

    @GetMapping(value = "/utilisateurs")
    List<UtilisateurDto> getAllUtilisateurs();

    @GetMapping(value = "/emprunts/{id}")
    List<LivreDto> getAllEmpruntsByUtilisateur(@PathVariable ("id") Integer utilisateurId);
}
