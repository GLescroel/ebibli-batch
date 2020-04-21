package com.ebibli.infrastructure.rest;


import com.ebibli.domain.EmpruntClient;
import com.ebibli.infrastructure.rest.emprunt.EmpruntClientApi;
import com.ebibli.infrastructure.rest.emprunt.RestEmpruntClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class RestConfig {

    @Bean
    public EmpruntClient restBiblios(EmpruntClientApi empruntClientApi) {
        return new RestEmpruntClient(empruntClientApi);
    }

    @Bean
    public CustomErrorDecoder customErrorDecoder() {
        return new CustomErrorDecoder();
    }
}
