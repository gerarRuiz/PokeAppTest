package com.ruizdev.pokeapptest.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruizdev.pokeapptest.util.Constants.POKEMON_DB_TABLE
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = POKEMON_DB_TABLE)
data class Pokemon(
    var name: String,
    var url: String,
    @Transient
    var favorite: Boolean = false
) {

    @PrimaryKey(autoGenerate = false)
    var id = url.split("/")[url.split("/").size - 2].toInt()

}
