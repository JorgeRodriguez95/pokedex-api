package com.example.pokedex.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PokemonSprites {
    @JsonProperty("front_default")
    private String frontDefault;
}
