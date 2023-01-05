package com.ruizdev.pokeapptest.domain.use_cases.local_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail

class InsertPokemonDetailsUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(apiResponsePokemonDetail: ApiResponsePokemonDetail) {
        repository.insertPokemonDetails(apiResponsePokemonDetail)
    }

}