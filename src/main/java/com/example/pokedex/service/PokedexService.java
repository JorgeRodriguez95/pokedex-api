package com.example.pokedex.service;

import com.example.pokedex.data.dto.DetailedPokemonDto;
import com.example.pokedex.data.dto.PokemonDto;

import java.util.List;

public interface PokedexService {

    List<PokemonDto> getPokemons(Integer offset, Integer limit);

    DetailedPokemonDto getPokemon(String name);
}
