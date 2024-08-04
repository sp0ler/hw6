package com.github.javarar.poke.controller;

import com.github.javarar.poke.model.Pokemon;
import com.github.javarar.poke.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class PokemonController {

    private final PokemonService pokemonService;

    /**
     * Все покемоны СИНХРОННО
     */
    @GetMapping("/getAll")
    public List<Pokemon> getAll(@RequestParam(value = "names") List<String> names) {
        return pokemonService.getAll(names);
    }

    /**
     * Рандомный покемон из списка СИНХРОННО
     */
    @GetMapping("/getAnyOne")
    public Pokemon getAnyOne(@RequestParam(value = "names") List<String> names) {
        return pokemonService.getAnyOne(names);
    }

    /**
     * Все покемоны ПАРАЛЛЕЛЬНО но блокируя
     */
    @GetMapping("/getAllAsync")
    public List<Pokemon> getAllAsync(@RequestParam(value = "names") List<String> names) {
        return pokemonService.getAllAsync(names);
    }

    /**
     * Рандомный покемон из списка ПАРАЛЛЕЛЬНО но блокируя
     */
    @GetMapping("/getAnyOneAsync")
    public Pokemon getAnyOneAsync(@RequestParam(value = "names") List<String> names) {
        return pokemonService.getAnyOneAsync(names);
    }

    /**
     * ШАГ 1
     * Все покимоны ПАРАЛЛЕЛЬНО
     */
    @GetMapping("/getTaskForAllAsync")
    public UUID getTaskForAllAsync(@RequestParam(value = "names") List<String> names) {
        return pokemonService.getTaskForAllAsync(names);
    }

    /**
     * ШАГ 2
     * Все покимоны ПАРАЛЛЕЛЬНО
     */
    @GetMapping("/getAllAsyncByTask")
    public List<Pokemon> getAllAsyncByTask(@RequestParam(value = "names") UUID uuid) {
        return pokemonService.getAllAsyncByTask(uuid);
    }
}
