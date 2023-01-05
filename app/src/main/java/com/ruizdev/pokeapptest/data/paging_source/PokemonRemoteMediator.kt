package com.ruizdev.pokeapptest.data.paging_source

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ruizdev.pokeapptest.data.local.PokemonDatabase
import com.ruizdev.pokeapptest.data.remote.PokemonApi
import com.ruizdev.pokeapptest.domain.model.Pokemon
import com.ruizdev.pokeapptest.domain.model.PokemonKeys

@ExperimentalPagingApi
class PokemonRemoteMediator(
    private val pokemonApi: PokemonApi,
    private val pokemonDatabase: PokemonDatabase
) : RemoteMediator<Int, Pokemon>() {

    private val TAG = "PokemonRemoteMediator"
    private val pokemonDao = pokemonDatabase.pokemonDao()
    private val pokemonKeysDao = pokemonDatabase.pokemonKeysDao()

    override suspend fun initialize(): InitializeAction {
        val currentTime = System.currentTimeMillis()
        val lastUpdated = pokemonKeysDao.getPokemonRemoteKeys(1)?.lastUpdated ?: 0L
        val cacheTimeout = 15

        val diffInMinutes = (currentTime - lastUpdated) / 1000 / 60

        return if (diffInMinutes.toInt() <= cacheTimeout) {

            Log.i(TAG, "initialize: UP TO DATE")
            InitializeAction.SKIP_INITIAL_REFRESH

        } else {
            Log.i(TAG, "initialize: REFRESH")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
        return try {

            val page = when (loadType) {

                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeysClosestToPosition(state)
                    remoteKeys?.nextPage?.minus(25) ?: 0
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysFirsTime(state)
                    val prevPage = remoteKeys?.previousPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysLastTime(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }

            }

            val response = pokemonApi.getPokemon(offset = page, limit = page + 25)
            val endOfPagination = response.body()?.results.isNullOrEmpty()

            val prevPage = if (page == 0) null else page - 25
            val nextPage = if (endOfPagination) null else page + 25


            pokemonDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.deleteAllPokemon()
                    pokemonKeysDao.deleteAllPokemonRemoteKeys()
                }

                val lastUpdated = System.currentTimeMillis()
                val keys = response.body()?.results?.map { pokemon ->
                    PokemonKeys(
                        id = pokemon.id.toLong(),
                        previousPage = prevPage,
                        nextPage = nextPage,
                        lastUpdated
                    )
                }
                pokemonKeysDao.addAllPokemonRemoteKeys(keys ?: listOf())
                pokemonDao.insertPokemon(response.body()?.results ?: listOf())
            }


            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeysFirsTime(state: PagingState<Int, Pokemon>): PokemonKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                pokemonKeysDao.getPokemonRemoteKeys(pokemon.id)
            }
    }

    private suspend fun getRemoteKeysLastTime(state: PagingState<Int, Pokemon>): PokemonKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { pokemon ->
            pokemonKeysDao.getPokemonRemoteKeys(pokemon.id)
        }
    }

    private suspend fun getRemoteKeysClosestToPosition(state: PagingState<Int, Pokemon>): PokemonKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                pokemonKeysDao.getPokemonRemoteKeys(id)
            }
        }
    }
}