package com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.util.network.Result

class GetPokemonDetailUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(pokemonId: Int): Result<ApiResponsePokemonDetail> {

        try {
            val response = repository.getPokemonDetail(pokemonId)
            if (!response.isSuccessful || response.body() == null) return Result.Error()
            return Result.Success(response.body()!!)
        } catch (e: Exception) {
            return Result.Error(error = e.message ?: "")
        }

    }

}