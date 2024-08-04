package com.github.javarar.poke.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(
            ObjectMapper objectMapper
    ) {
        var uriFactory = new DefaultUriBuilderFactory();
        uriFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        var strConverter = new FormHttpMessageConverter();
        var jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);

        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60_000);
        requestFactory.setReadTimeout(60_000);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setMessageConverters(List.of(strConverter, jsonConverter));
        restTemplate.setUriTemplateHandler(uriFactory);
        return restTemplate;
    }

    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(5);
    }
}
