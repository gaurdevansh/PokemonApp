package com.example.pokemonapp.utils

import com.example.pokemonapp.data.Pokemon

interface PokemonFetchCallback {
    fun onPokemonFetched(pokemon: Pokemon)
    fun onError(error: String)
}