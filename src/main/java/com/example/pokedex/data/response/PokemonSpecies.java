package com.example.pokedex.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonSpecies {
    @JsonProperty("evolution_chain")
    private NamedResource evolutionChain;
    @JsonProperty("flavor_text_entries")
    private List<FlavorText> flavorText;
}
