package com.example.pokedex.controller;

import com.example.pokedex.data.dto.DetailedPokemonDto;
import com.example.pokedex.data.dto.PokemonDto;
import com.example.pokedex.service.PokedexService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    public List<PokemonDto> getPokemons(Integer offset, Integer limit){
        return pokedexService.getPokemons(offset, limit);
    }

    public DetailedPokemonDto getPokemon(String name){
        return pokedexService.getPokemon(name);
    }
}
