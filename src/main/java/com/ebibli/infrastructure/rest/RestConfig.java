package com.ebibli.infrastructure.rest;


import com.ebibli.domain.BiblioClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class RestConfig {

    @Bean
    public BiblioClients restBiblios(BiblioClientApi biblioClientApi) {
        return new RestBiblioClients(biblioClientApi);
    }
}
