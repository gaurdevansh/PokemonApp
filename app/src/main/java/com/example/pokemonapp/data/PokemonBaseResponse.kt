package com.example.pokemonapp.data

data class PokemonBaseResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonBase>
)