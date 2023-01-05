package com.ruizdev.pokeapptest.di

import android.content.Context
import androidx.annotation.RequiresPermission.Read
import com.ruizdev.pokeapptest.data.repository.DataStoreOperationsImpl
import com.ruizdev.pokeapptest.data.repository.Repository
import com.ruizdev.pokeapptest.domain.repository.DataStoreOperations
import com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases.*
import com.ruizdev.pokeapptest.domain.use_cases.local_use_cases.InsertPokemonDetailsUseCase
import com.ruizdev.pokeapptest.domain.use_cases.local_use_cases.LocalUseCases
import com.ruizdev.pokeapptest.domain.use_cases.local_use_cases.MarkPokemonFavoriteuseCase
import com.ruizdev.pokeapptest.domain.use_cases.local_use_cases.ReadPokemonDetailsUseCase
import com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases.GetAllPokemonUseCase
import com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases.GetPokemonDetailUseCase
import com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases.RemoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesDataStoreOperations(
        @ApplicationContext context: Context
    ): DataStoreOperations {
        return DataStoreOperationsImpl(context)
    }

    @Provides
    @Singleton
    fun providesRemoteUseCases(repository: Repository): RemoteUseCases {
        return RemoteUseCases(
            getAllPokemon = GetAllPokemonUseCase(repository),
            getPokemonDetailUseCase = GetPokemonDetailUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providesDataStoreOperationsUseCases(repository: Repository): DataStoreUseCases {
        return DataStoreUseCases(
            saveAvatarUrlUseCase = SaveAvatarUrlUseCase(repository),
            readAvatarUrlUseCase = ReadAvatarUrlUseCase(repository),
            saveWordUseCase = SaveWordUseCase(repository),
            readWordUseCase = ReadWordUseCase(repository),
            saveBackgroundColorUseCase = SaveBackgroundColorUseCase(repository),
            readBackgroundColorUseCase = ReadBackgroundColorUseCase(repository),
            saveTextColorUseCase = SaveTextColorUseCase(repository),
            readTextColorUseCase = ReadTextColorUseCase(repository),
            saveSessionUUIDUseCase = SaveSessionUUIDUseCase(repository),
            readSessionUUIDUseCase = ReadSessionUUIDUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providesLocalUseCases(repository: Repository): LocalUseCases {
        return LocalUseCases(
            insertPokemonDetailsUseCase = InsertPokemonDetailsUseCase(repository),
            readPokemonDetailsUseCase = ReadPokemonDetailsUseCase(repository),
            markPokemonFavoriteuseCase = MarkPokemonFavoriteuseCase(repository)
        )
    }

}