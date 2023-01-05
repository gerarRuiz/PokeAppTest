package com.ruizdev.pokeapptest.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ruizdev.pokeapptest.data.local.PokemonDatabase
import com.ruizdev.pokeapptest.data.paging_source.PokemonRemoteMediator
import com.ruizdev.pokeapptest.data.remote.PokemonApi
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.model.Pokemon
import com.ruizdev.pokeapptest.domain.repository.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

@ExperimentalPagingApi
class RemoteDataSourceImpl(
    private val pokemonApi: PokemonApi,
    private val pokemonDatabase: PokemonDatabase
) : RemoteDataSource {

    private val pokemonDao = pokemonDatabase.pokemonDao()

    override suspend fun getPokemonDetail(pokemonId: Int): Response<ApiResponsePokemonDetail> {
        return pokemonApi.getPokemonDetail(pokemonId)
    }

    override fun getAllPokemon(): Flow<PagingData<Pokemon>> {
        val pagingSource = { pokemonDao.getAllPokemon() }

        return Pager(
            config = PagingConfig(pageSize = 25, maxSize = 1194),
            remoteMediator = PokemonRemoteMediator(
                pokemonApi, pokemonDatabase
            ),
            pagingSourceFactory = pagingSource
        ).flow
    }
}