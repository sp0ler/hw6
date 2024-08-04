package com.github.javarar.poke.service;

import com.github.javarar.poke.model.Pokemon;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Log4j2
@Component
@RequiredArgsConstructor
public class PokemonClient {

    public static final String PATH_FORMATED = "https://pokeapi.co/api/v2/pokemon/%s";

    private final RestTemplate restTemplate;

    @SneakyThrows
    public Pokemon getByName(String name) {
        log.info("Отправлен запрос на получение покемона: {}", name);
        Pokemon body = restTemplate.getForEntity(PATH_FORMATED.formatted(name), Pokemon.class).getBody();
        Thread.sleep(nextLong(1_000, 3_000));
        log.info("Покемон получен: {}", body);
        return body;
    }

    private long nextLong(long leftLimit, long rightLimit) {
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
