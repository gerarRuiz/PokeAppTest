package com.ruizdev.pokeapptest.data.remote

import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemon
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon/")
    suspend fun getPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ApiResponsePokemon>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int,
    ): Response<ApiResponsePokemonDetail>

}