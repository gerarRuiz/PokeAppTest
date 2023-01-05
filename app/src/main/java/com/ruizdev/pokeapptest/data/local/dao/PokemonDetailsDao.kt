package com.ruizdev.pokeapptest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail

@Dao
interface PokemonDetailsDao {

    @Query("SELECT * FROM POKEMON_DETAIL_DB_TABLE where id=:id")
    fun getDetailsPokemon(id: Int): ApiResponsePokemonDetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonDetail: ApiResponsePokemonDetail)

    @Query("DELETE FROM POKEMON_DETAIL_DB_TABLE")
    suspend fun deleteAllPokemonDetails()

}