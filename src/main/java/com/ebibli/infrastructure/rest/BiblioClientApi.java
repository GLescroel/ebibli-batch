package com.ebibli.infrastructure.rest;

import com.ebibli.dto.UtilisateurDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "biblio-services",
        url = "${clients.com-ebibli-v1-vs.endpoint}")
public interface BiblioClientApi {

    @GetMapping(value = "/utilisateurs")
    List<UtilisateurDto> getAllUtilisateurs();
}
