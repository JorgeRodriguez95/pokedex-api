package com.example.pokedex.boundary;

import com.example.pokedex.controller.PokedexController;
import com.example.pokedex.data.dto.DetailedPokemonDto;
import com.example.pokedex.data.dto.PokemonDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokedex")
@CrossOrigin("*")
public class PokedexBoundary {

    private final PokedexController pokedexController;

    public PokedexBoundary(PokedexController pokedexController) {
        this.pokedexController = pokedexController;
    }

    @GetMapping
    public ResponseEntity<List<PokemonDto>> getPokemons(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1126") Integer limit){
        return new ResponseEntity<>(pokedexController.getPokemons(offset, limit), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<DetailedPokemonDto> getPokemon(@PathVariable String name){
        return new ResponseEntity<>(pokedexController.getPokemon(name), HttpStatus.OK);
    }
}
