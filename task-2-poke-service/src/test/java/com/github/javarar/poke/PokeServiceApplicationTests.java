package com.github.javarar.poke;

import com.github.javarar.poke.model.Pokemon;
import com.github.javarar.poke.service.PokemonClient;
import com.github.javarar.poke.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PokeServiceApplicationTests {

    private static final List<String> list = List.of("ditto", "abomasnow");

    @MockBean
    private PokemonClient pokemonClient;

    @Autowired
    private PokemonService pokemonService;

    @Test
    void getAll() {
        mockPokemonClient();

        List<Pokemon> pokemons = assertDoesNotThrow(() -> pokemonService.getAll(list));
        assertEquals(list.size(), pokemons.size());
    }

    @Test
    void getAnyOne() {
        mockPokemonClient();

        Pokemon pokemon = assertDoesNotThrow(() -> pokemonService.getAnyOne(list));
        assertTrue(list.contains(pokemon.getName()));
    }

    @Test
    void getAllAsync() {
        mockPokemonClient();

        List<Pokemon> pokemons = assertDoesNotThrow(() -> pokemonService.getAllAsync(list));
        assertEquals(list.size(), pokemons.size());
    }

    @Test
    void getAnyOneAsync() {
        mockPokemonClient();

        Pokemon pokemon = assertDoesNotThrow(() -> pokemonService.getAnyOneAsync(list));
        assertTrue(list.contains(pokemon.getName()));
    }

    @Test
    void getTaskForAllAsync() throws InterruptedException {
        mockPokemonClient();

        UUID uuid = assertDoesNotThrow(() -> pokemonService.getTaskForAllAsync(list));
        assertThrows(RuntimeException.class, () -> pokemonService.getAllAsyncByTask(uuid));

        Thread.sleep(5_000);

        List<Pokemon> pokemons = assertDoesNotThrow(() -> pokemonService.getAllAsyncByTask(uuid));
        assertEquals(list.size(), pokemons.size());
    }

    private void mockPokemonClient() {
        Pokemon ditto = new Pokemon("ditto");
        Pokemon abomasnow = new Pokemon("abomasnow");

        when(pokemonClient.getByName("ditto")).thenReturn(ditto);
        when(pokemonClient.getByName("abomasnow")).thenReturn(abomasnow);
    }

}
