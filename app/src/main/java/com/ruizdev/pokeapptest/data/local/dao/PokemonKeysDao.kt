package com.ruizdev.pokeapptest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruizdev.pokeapptest.domain.model.PokemonKeys

@Dao
interface PokemonKeysDao {

    @Query("SELECT * FROM POKEMON_KEYS_TABLE where ID=:id")
    suspend fun getPokemonRemoteKeys(id: Int): PokemonKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllPokemonRemoteKeys(pokemonKeys: List<PokemonKeys>)

    @Query("DELETE FROM POKEMON_KEYS_TABLE")
    suspend fun deleteAllPokemonRemoteKeys()

}