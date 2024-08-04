package com.github.javarar.poke.service;

import com.github.javarar.poke.cache.InMemoryCache;
import com.github.javarar.poke.model.Pokemon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final Random random = ThreadLocalRandom.current();

    private final InMemoryCache inMemoryCache;
    private final PokemonClient pokemonClient;
    private final Executor executor;

    public List<Pokemon> getAll(List<String> names) {
        return names.stream()
                .map(pokemonClient::getByName)
                .toList();
    }

    public Pokemon getAnyOne(List<String> names) {
        int index = random.nextInt(names.size());
        String value = names.get(index);
        return pokemonClient.getByName(value);

    }

    public List<Pokemon> getAllAsync(List<String> names) {
        List<CompletableFuture<Pokemon>> list = names.stream()
                .map(it -> CompletableFuture.supplyAsync(() -> pokemonClient.getByName(it), executor))
                .toList();

        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();

        return list.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    public Pokemon getAnyOneAsync(List<String> names) {
        List<CompletableFuture<Pokemon>> list = names.stream()
                .map(it -> CompletableFuture.supplyAsync(() -> pokemonClient.getByName(it), executor))
                .toList();

        return (Pokemon) CompletableFuture.anyOf(list.toArray(new CompletableFuture[0])).join();
//
//        return list.stream()
//                .filter(CompletableFuture::isDone)
//                .map(CompletableFuture::join)
//                .toList();

    }

    public UUID getTaskForAllAsync(List<String> names) {
        final UUID taskUUID = inMemoryCache.getTask();

        executor.execute(() -> {
            List<CompletableFuture<Pokemon>> list = names.stream()
                    .map(it -> CompletableFuture.supplyAsync(() -> pokemonClient.getByName(it), executor))
                    .toList();

            CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();

            List<Pokemon> result = list.stream().map(CompletableFuture::join).toList();
            inMemoryCache.setPokemon(result, taskUUID);
        });

        return taskUUID;
    }

    public List<Pokemon> getAllAsyncByTask(UUID uuid) {
        return inMemoryCache.getPokemon(uuid);
    }
}
