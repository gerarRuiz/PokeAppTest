package com.ruizdev.pokeapptest.di

import android.content.Context
import androidx.room.Room
import com.ruizdev.pokeapptest.data.local.PokemonDatabase
import com.ruizdev.pokeapptest.data.repository.LocalDataSourceImpl
import com.ruizdev.pokeapptest.domain.repository.LocalDataSource
import com.ruizdev.pokeapptest.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesLocalDataSource(
        pokemonDatabase: PokemonDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(
            pokemonDatabase
        )
    }

}