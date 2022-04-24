package com.example.pokedex.service.impl;

import com.example.pokedex.config.exception.ResourceNotFoundException;
import com.example.pokedex.data.dto.DetailedPokemonDto;
import com.example.pokedex.data.dto.PokemonDto;
import com.example.pokedex.data.response.EvolutionChain;
import com.example.pokedex.data.response.PokemonObject;
import com.example.pokedex.data.response.PokemonResponse;
import com.example.pokedex.data.response.PokemonSpecies;
import com.example.pokedex.helper.PokedexHelper;
import com.example.pokedex.proxi.api.PokedexApi;
import com.example.pokedex.service.PokedexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@EnableCaching
@Service
public class PokedexServiceImpl implements PokedexService {

    private final PokedexApi pokedexApi;
    private final PokedexHelper pokedexHelper;

    public PokedexServiceImpl(PokedexApi pokedexApi, PokedexHelper pokedexHelper) {
        this.pokedexApi = pokedexApi;
        this.pokedexHelper = pokedexHelper;
    }

    @Override
    @Cacheable("pokemons")
    public List<PokemonDto> getPokemons(Integer offset, Integer limit) {
        PokemonResponse pokemonResponse = getPokemonsApi(offset, limit);
        if(null != pokemonResponse){
            return pokemonResponse.getResults().parallelStream()
                    .map(resource -> getPokemonApi(resource.getName())).filter(Objects::nonNull)
                    .map(pokedexHelper::toPokemonDto)
                    .toList();

        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable("pokemon")
    public DetailedPokemonDto getPokemon(String name) {
        log.info("Search pokemon: {}", name);
        PokemonObject pokemon = getPokemonApi(name.toLowerCase());
        if(Objects.isNull(pokemon)){
            throw new ResourceNotFoundException();
        }
        PokemonSpecies species = getSpecies(pokedexHelper.getIdUrlSpecies(pokemon.getSpecies().getUrl()));
        if(Objects.isNull(species)){
            throw new ResourceNotFoundException();
        }
        EvolutionChain evolutions = getEvolutions(pokedexHelper.getIdUrlEvolution(species.getEvolutionChain().getUrl()));
        return pokedexHelper.getDetailPokemon(pokemon, evolutions, species);
    }


    private PokemonResponse getPokemonsApi(Integer offset, Integer limit){
        try {
            Response<PokemonResponse> response = pokedexApi.getPokemons(limit, offset).execute();
            return pokedexHelper.checkResponse(response, "Error getting data in Pokedex api");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PokemonObject getPokemonApi(String name){
        try {
            Response<PokemonObject> response = pokedexApi.getPokemon(name).execute();
            return pokedexHelper.checkResponse(response, "Error getting Pokemon data in Pokedex api");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private EvolutionChain getEvolutions(String id){
        try {
            Response<EvolutionChain> response = pokedexApi.getEvolutions(id).execute();
            return pokedexHelper.checkResponse(response, "Error getting evolutions data in Pokedex api");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PokemonSpecies getSpecies(String id){
        try {
            Response<PokemonSpecies> response = pokedexApi.getSpecies(id).execute();
            return pokedexHelper.checkResponse(response, "Error getting species data in Pokedex api");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
