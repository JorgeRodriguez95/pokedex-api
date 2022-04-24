package com.example.pokedex.proxi.api;

import com.example.pokedex.data.response.EvolutionChain;
import com.example.pokedex.data.response.PokemonObject;
import com.example.pokedex.data.response.PokemonResponse;
import com.example.pokedex.data.response.PokemonSpecies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokedexApi {

    @GET("pokemon")
    Call<PokemonResponse> getPokemons(@Query("limit") Integer limit, @Query("offset") Integer integer);

    @GET("pokemon/{name}")
    Call<PokemonObject> getPokemon(@Path("name") String name);

    @GET("evolution-chain/{id}/")
    Call<EvolutionChain> getEvolutions(@Path("id") String id);

    @GET("pokemon-species/{id}/")
    Call<PokemonSpecies> getSpecies(@Path("id") String id);
}
