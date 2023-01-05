package com.ruizdev.pokeapptest.domain.use_cases.local_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail

class ReadPokemonDetailsUseCase(
    private val repository: Repository
) {

    operator fun invoke(pokemonId: Int): ApiResponsePokemonDetail {
        return repository.readPokemonDetails(pokemonId)
    }

}