package com.ruizdev.pokeapptest.domain.repository

import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun readPokemonDetails(idPokemon: Int): ApiResponsePokemonDetail

    suspend fun insertPokemonDetails(apiResponsePokemonDetail: ApiResponsePokemonDetail)

    suspend fun markPokemonFavorite(idPokemon: Long, favorite: Boolean)

}