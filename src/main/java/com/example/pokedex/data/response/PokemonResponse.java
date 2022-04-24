package com.example.pokedex.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<NamedResource> results;
}
