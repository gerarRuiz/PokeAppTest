package com.ruizdev.pokeapptest.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruizdev.pokeapptest.util.Constants.POKEMON_KEYS_DB_TABLE

@Entity(tableName = POKEMON_KEYS_DB_TABLE)
data class PokemonKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val previousPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long?
)
