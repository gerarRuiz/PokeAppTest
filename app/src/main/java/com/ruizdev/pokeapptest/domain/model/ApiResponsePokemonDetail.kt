package com.ruizdev.pokeapptest.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruizdev.pokeapptest.util.Constants.POKEMON_DETAIL_DB_TABLE
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = POKEMON_DETAIL_DB_TABLE)
data class ApiResponsePokemonDetail(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var name: String,
    var height: Int,
    var weight: Int,
    var sprites: Sprites,
    var types: List<Types>
)

@Serializable
data class Sprites(
    var front_default: String
)

@Serializable
data class Types(
    var type: TypeDetail
)

@Serializable
data class TypeDetail(
    var name: String
)