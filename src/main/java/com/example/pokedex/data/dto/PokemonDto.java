package com.example.pokedex.data.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public class PokemonDto {
    private String name;
    private String type;
    private Integer weight;
    private String image;
    private List<String> abilities;
}
