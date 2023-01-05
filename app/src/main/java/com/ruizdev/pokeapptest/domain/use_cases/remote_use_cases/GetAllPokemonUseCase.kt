package com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases

import androidx.paging.PagingData
import com.ruizdev.pokeapptest.data.repository.Repository
import com.ruizdev.pokeapptest.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

class GetAllPokemonUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<PagingData<Pokemon>> {
        return repository.getAllPokemon()
    }

}