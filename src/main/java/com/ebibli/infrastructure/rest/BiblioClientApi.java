package com.ebibli.infrastructure.rest;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "biblio-services",
        url = "${clients.com-ebibli-v1-vs.endpoint}")
public interface BiblioClientApi {

}
