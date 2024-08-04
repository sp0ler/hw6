package com.github.javarar.poke.cache;

import com.github.javarar.poke.model.Pokemon;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryCache {

    private static final Map<UUID, List<Pokemon>> cache = new ConcurrentHashMap<>();

    public UUID getTask() {
        UUID uuid = UUID.randomUUID();
        cache.put(uuid, Collections.emptyList());
        return uuid;
    }

    public void setPokemon(List<Pokemon> list, UUID uuid) {
        cache.put(uuid, list);

    }

    public List<Pokemon> getPokemon(UUID uuid) {
        List<Pokemon> pokemons = cache.get(uuid);
        cache.remove(uuid);
        if (pokemons.isEmpty()) {
            throw new RuntimeException("Покемоны не готовы!");
        }
        return pokemons;
    }
}
