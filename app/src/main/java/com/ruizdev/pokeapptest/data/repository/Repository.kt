package com.ruizdev.pokeapptest.data.repository

import androidx.paging.PagingData
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.model.Pokemon
import com.ruizdev.pokeapptest.domain.repository.DataStoreOperations
import com.ruizdev.pokeapptest.domain.repository.LocalDataSource
import com.ruizdev.pokeapptest.domain.repository.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dataStoreOperations: DataStoreOperations
) {

    /**
     * Remote
     */

    fun getAllPokemon(): Flow<PagingData<Pokemon>> {
        return remote.getAllPokemon()
    }

    suspend fun getPokemonDetail(pokemonId: Int): Response<ApiResponsePokemonDetail> {
        return remote.getPokemonDetail(pokemonId)
    }

    /**
     * Local
     */

    suspend fun insertPokemonDetails(apiResponsePokemonDetail: ApiResponsePokemonDetail) {
        local.insertPokemonDetails(apiResponsePokemonDetail)
    }

    fun readPokemonDetails(pokemonId: Int): ApiResponsePokemonDetail {
        return local.readPokemonDetails(pokemonId)
    }

    suspend fun markFavorite(idPokemon: Long, favorite: Boolean) {
        return local.markPokemonFavorite(idPokemon, favorite)
    }

    /**
     * Data Store
     */

    suspend fun saveAvatarUrl(avatarUrl: String) {
        dataStoreOperations.saveAvatarUrl(avatarUrl)
    }

    fun readAvatarUrl(): Flow<String> {
        return dataStoreOperations.readAvatarUrl()
    }

    suspend fun saveWord(word: String) {
        dataStoreOperations.saveWord(word)
    }

    fun readWord(): Flow<String> {
        return dataStoreOperations.readWord()
    }

    suspend fun saveBackgroundColor(color: Int) {
        dataStoreOperations.saveBackgroundColor(color)
    }

    fun readBackgroundColor(): Flow<Int> {
        return dataStoreOperations.readBackgroundColor()
    }

    suspend fun saveTextColor(color: Int) {
        dataStoreOperations.saveTextColor(color)
    }

    fun readTextColor(): Flow<Int> {
        return dataStoreOperations.readTextColor()
    }

    suspend fun saveSessionUUID(sessionUUID: String){
        dataStoreOperations.saveSessionUUID(sessionUUID)
    }

    fun readSessionUUID(): Flow<String> {
        return dataStoreOperations.readSessionUUID()
    }

}