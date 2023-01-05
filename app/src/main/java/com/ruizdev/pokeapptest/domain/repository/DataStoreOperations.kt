package com.ruizdev.pokeapptest.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveAvatarUrl(url: String)

    fun readAvatarUrl(): Flow<String>

    suspend fun saveWord(word: String)

    fun readWord(): Flow<String>

    suspend fun saveBackgroundColor(color: Int)

    fun readBackgroundColor(): Flow<Int>

    suspend fun saveTextColor(color: Int)

    fun readTextColor(): Flow<Int>

    suspend fun saveSessionUUID(uuid: String)

    fun readSessionUUID(): Flow<String>

}