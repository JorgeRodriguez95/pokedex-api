package com.example.pokedex.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonObject {
    private Integer id;
    private String name;
    private Integer weight;
    private PokemonSprites sprites;
    private List<PokemonType> types;
    private List <PokemonAbility> abilities;
    private NamedResource species;
}
