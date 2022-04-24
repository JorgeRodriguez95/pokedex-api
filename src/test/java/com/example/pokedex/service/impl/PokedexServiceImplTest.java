package com.example.pokedex.service.impl;

import com.example.pokedex.config.exception.ResourceNotFoundException;
import com.example.pokedex.data.dto.DetailedPokemonDto;
import com.example.pokedex.data.dto.PokemonDto;
import com.example.pokedex.data.response.*;
import com.example.pokedex.helper.PokedexHelper;
import com.example.pokedex.proxi.api.PokedexApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(PokedexServiceImplTest.class)
@AutoConfigureMockMvc
class PokedexServiceImplTest {

    @InjectMocks
    PokedexServiceImpl pokedexService;

    @Mock
    PokedexApi pokedexApi;

    @Mock
    PokedexHelper pokedexHelper;

    @Mock
    Call<PokemonResponse> pokemonResponseCall;

    @Mock
    Call<PokemonObject> pokemonObjectCall;

    @Mock
    Call<PokemonSpecies> pokemonSpeciesCall;

    @Mock
    Call<EvolutionChain> evolutionChainCall;

    PokemonResponse pokemonResponse;

    PokemonObject object;

    PokemonSpecies pokemonSpecies;

    EvolutionChain evolutionChain;

    @BeforeEach
    void setUp() {

        List<NamedResource> list = new ArrayList<>();
        NamedResource resource = NamedResource
                .builder()
                .name("bulbasaur")
                .url("https://pokeapi.co/api/v2/pokemon/1/")
                .build();
        list.add(resource);
        pokemonResponse = PokemonResponse.builder()
                .results(list)
                .build();

        object = PokemonObject.builder()
                .id(1)
                .name("bulbasaur")
                .species(resource)
                .build();
        pokemonSpecies = PokemonSpecies.builder()
                .evolutionChain(resource)
                .build();

        evolutionChain = EvolutionChain.builder()
                .chain(ChainUrl.builder()
                        .species(resource)
                        .build())
                .build();
    }

    @Test
    void getPokemons_caseSuccess() throws IOException {

        when(pokedexApi.getPokemons(any(), any())).thenReturn(pokemonResponseCall);

        when(pokemonResponseCall.execute()).thenReturn(Response.success(pokemonResponse));

        when(pokedexHelper.checkResponse(ArgumentMatchers.any(), anyString())).
                thenReturn(pokemonResponse)
                .thenReturn(object);

        when(pokedexApi.getPokemon(any())).thenReturn(pokemonObjectCall);

        when(pokemonObjectCall.execute()).thenReturn(Response.success(object));


        List<PokemonDto> pokemonResponse = pokedexService.getPokemons(any(), any());

        assertTrue(!pokemonResponse.isEmpty());
    }

    @Test
    void getPokemons_casePokemonsNotFound() throws IOException {

        when(pokedexApi.getPokemons(any(), any())).thenReturn(pokemonResponseCall);

        when(pokemonResponseCall.execute()).thenThrow(new IOException("test"));

        List<PokemonDto> pokemonResponse = pokedexService.getPokemons(any(), any());

        assertTrue(pokemonResponse.isEmpty());
    }

    @Test
    void getPokemon_caseSuccess() throws IOException {

        when(pokedexApi.getPokemon(any())).thenReturn(pokemonObjectCall);

        when(pokemonObjectCall.execute()).thenReturn(Response.success(object));

        when(pokedexHelper.checkResponse(ArgumentMatchers.any(), anyString())).thenReturn(object).
                thenReturn(pokemonSpecies)
                .thenReturn(evolutionChain);

        when(pokedexApi.getSpecies(any())).thenReturn(pokemonSpeciesCall);

        when(pokemonSpeciesCall.execute()).thenReturn(Response.success(pokemonSpecies));

        when(pokedexApi.getEvolutions(any())).thenReturn(evolutionChainCall);

        when(evolutionChainCall.execute()).thenReturn(Response.success(evolutionChain));

        when(pokedexHelper.getDetailPokemon(any(), any(), any())).thenReturn(DetailedPokemonDto.builder()
                .name("bulbasaur")
                .build());

        DetailedPokemonDto detailedPokemonDto = pokedexService.getPokemon("bulbasaur");

        assertTrue(!Objects.isNull(detailedPokemonDto));
    }

    @Test
    void getPokemon_casePokemonNotFound() throws IOException {

        when(pokedexApi.getPokemon(any())).thenReturn(pokemonObjectCall);

        when(pokemonObjectCall.execute()).thenThrow(new IOException("test"));

        assertThrows(ResourceNotFoundException.class, () -> pokedexService.getPokemon("bulbasaur"));
    }

    @Test
    void getPokemon_caseSpecieNotFound() throws IOException {

        when(pokedexApi.getPokemon(any())).thenReturn(pokemonObjectCall);

        when(pokemonObjectCall.execute()).thenReturn(Response.success(object));

        when(pokedexHelper.checkResponse(ArgumentMatchers.any(), anyString())).thenReturn(object);

        when(pokedexApi.getSpecies(any())).thenReturn(pokemonSpeciesCall);

        when(pokemonSpeciesCall.execute()).thenThrow(new IOException("test"));

        assertThrows(ResourceNotFoundException.class, () -> pokedexService.getPokemon("bulbasaur"));
    }

    @Test
    void getPokemon_caseEvolutionNotFound() throws IOException {

        when(pokedexApi.getPokemon(any())).thenReturn(pokemonObjectCall);

        when(pokemonObjectCall.execute()).thenReturn(Response.success(object));

        when(pokedexHelper.checkResponse(ArgumentMatchers.any(), anyString())).thenReturn(object).
                thenReturn(pokemonSpecies);

        when(pokedexApi.getSpecies(any())).thenReturn(pokemonSpeciesCall);

        when(pokemonSpeciesCall.execute()).thenReturn(Response.success(pokemonSpecies));

        when(pokedexApi.getEvolutions(any())).thenReturn(evolutionChainCall);

        when(evolutionChainCall.execute()).thenThrow(new IOException("test"));

        when(pokedexHelper.getDetailPokemon(any(), any(), any())).thenReturn(DetailedPokemonDto.builder()
                .name("bulbasaur")
                .build());

        DetailedPokemonDto detailedPokemonDto = pokedexService.getPokemon("bulbasaur");

        assertTrue(Objects.isNull(detailedPokemonDto.getEvolutions()));
    }
}