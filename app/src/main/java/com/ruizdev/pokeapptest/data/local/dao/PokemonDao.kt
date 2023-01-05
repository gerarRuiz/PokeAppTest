package com.ruizdev.pokeapptest.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruizdev.pokeapptest.domain.model.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM POKEMON_DB_TABLE ORDER BY id ASC")
    fun getAllPokemon(): PagingSource<Int, Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: List<Pokemon>)

    @Query("DELETE FROM POKEMON_DB_TABLE")
    suspend fun deleteAllPokemon()

    @Query("UPDATE pokemon_db_table SET favorite=:favorite where id=:id")
    fun markFavorite(id: Long, favorite: Boolean)

}