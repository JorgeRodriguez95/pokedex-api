package com.example.pokedex.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class DetailedPokemonDto extends PokemonDto {
    private String description;
    private List<String> evolutions;
}
