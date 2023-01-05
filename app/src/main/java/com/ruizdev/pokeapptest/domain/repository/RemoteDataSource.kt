package com.ruizdev.pokeapptest.domain.repository

import androidx.paging.PagingData
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {

    fun getAllPokemon(): Flow<PagingData<Pokemon>>

    suspend fun getPokemonDetail(pokemonId: Int): Response<ApiResponsePokemonDetail>

}