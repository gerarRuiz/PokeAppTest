package com.ruizdev.pokeapptest.di

import androidx.paging.ExperimentalPagingApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ruizdev.pokeapptest.data.local.PokemonDatabase
import com.ruizdev.pokeapptest.data.remote.PokemonApi
import com.ruizdev.pokeapptest.data.repository.RemoteDataSourceImpl
import com.ruizdev.pokeapptest.domain.repository.RemoteDataSource
import com.ruizdev.pokeapptest.util.Constants.BASE_URL
import com.ruizdev.pokeapptest.util.Constants.CONNECTION_TIMEOUT
import com.ruizdev.pokeapptest.util.Constants.READ_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalPagingApi
@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {

        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    }

    @Provides
    @Singleton
    fun providesPokemonApi(retrofit: Retrofit): PokemonApi {
        return retrofit.create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providesRemoteDataSource(
        pokemonApi: PokemonApi,
        pokemonDatabase: PokemonDatabase
    ): RemoteDataSource {
        return RemoteDataSourceImpl(pokemonApi, pokemonDatabase)
    }

}