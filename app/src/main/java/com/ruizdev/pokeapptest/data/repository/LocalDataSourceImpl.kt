package com.ruizdev.pokeapptest.data.repository

import com.ruizdev.pokeapptest.data.local.PokemonDatabase
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.repository.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    pokemonDatabase: PokemonDatabase
) : LocalDataSource {

    private val pokemonDao = pokemonDatabase.pokemonDao()
    private val pokemonDetailsDao = pokemonDatabase.pokemonDetailsDao()

    override fun readPokemonDetails(idPokemon: Int): ApiResponsePokemonDetail {
        return pokemonDetailsDao.getDetailsPokemon(idPokemon)
    }

    override suspend fun insertPokemonDetails(apiResponsePokemonDetail: ApiResponsePokemonDetail) {
        pokemonDetailsDao.insertPokemonDetails(apiResponsePokemonDetail)
    }

    override suspend fun markPokemonFavorite(idPokemon: Long, favorite: Boolean) {
        pokemonDao.markFavorite(idPokemon, favorite)
    }

}