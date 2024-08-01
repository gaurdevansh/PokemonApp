package com.example.pokemonapp.remote

import com.example.pokemonapp.data.Pokemon
import com.example.pokemonapp.data.PokemonBaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonBaseResponse

    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): Call<Pokemon>
}