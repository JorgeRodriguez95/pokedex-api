package com.example.pokedex.helper;

import com.example.pokedex.data.dto.DetailedPokemonDto;
import com.example.pokedex.data.dto.PokemonDto;
import com.example.pokedex.data.response.*;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PokedexHelper {

    public <T> T checkResponse(Response<T> response, String message) throws IOException {
        if (response.isSuccessful()) {
            return response.body();
        } else {
            try {
                if (response.errorBody() != null) {
                    throw new IOException(message + ": " + response.errorBody().string());
                }
                throw new IOException(message + ": NULL RESPONSE BODY RECEIVED");
            } catch (IOException e) {
                throw new IOException(message + ": Error getting error response body");
            }
        }
    }

    public PokemonDto toPokemonDto(PokemonObject object){
        return PokemonDto.builder()
                .name(object.getName())
                .weight(object.getWeight())
                .image(object.getSprites().getFrontDefault())
                .type(object.getTypes().stream()
                        .map(PokemonType::getType)
                        .map(NamedResource::getName)
                        .collect(Collectors.joining(", ")))
                .abilities(object.getAbilities().stream()
                        .map(PokemonAbility::getAbility)
                        .map(NamedResource::getName)
                        .toList())
                .build();
    }

    public String getDescriptionEn(PokemonSpecies pokemonSpecies){
        return pokemonSpecies.getFlavorText()
                .stream()
                .filter(len -> len.getLanguage().getName().equals("en"))
                .map(FlavorText::getDescription)
                .findFirst()
                .orElse("");
    }

    public DetailedPokemonDto getDetailPokemon(PokemonObject pokemon, EvolutionChain evolutionChain, PokemonSpecies pokemonSpecies){
        return DetailedPokemonDto.builder()
                .name(pokemon.getName())
                .type(pokemon.getTypes().stream()
                        .map(PokemonType::getType)
                        .map(NamedResource::getName)
                        .collect(Collectors.joining(", ")))
                .weight(pokemon.getWeight())
                .image(pokemon.getSprites().getFrontDefault())
                .abilities(pokemon.getAbilities().stream()
                        .map(PokemonAbility::getAbility)
                        .map(NamedResource::getName)
                        .toList())
                .evolutions(getEvolutions(evolutionChain))
                .description(getDescriptionEn(pokemonSpecies))
                .build();
    }

    public String getIdUrlEvolution(String url){
        String[] parts = url.split("evolution-chain/");
        return parts[1];
    }

    public String getIdUrlSpecies(String url){
        String[] parts = url.split("pokemon-species/");
        return parts[1];
    }
    private List<String> getEvolutions(EvolutionChain evolutions){
        List<String> evolutionsOut = new ArrayList<>();
        ChainUrl chainUrl = evolutions.getChain();

        while(null != chainUrl){
            evolutionsOut.add(chainUrl.getSpecies().getName());
            chainUrl = !chainUrl.getEvolvesTo().isEmpty() ? chainUrl.getEvolvesTo().get(0) : null;
        }
        evolutionsOut.remove(0);
        return evolutionsOut;
    }

}
