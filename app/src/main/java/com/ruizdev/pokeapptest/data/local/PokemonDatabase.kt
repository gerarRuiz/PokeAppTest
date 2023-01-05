package com.ruizdev.pokeapptest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ruizdev.pokeapptest.data.local.dao.PokemonDao
import com.ruizdev.pokeapptest.data.local.dao.PokemonDetailsDao
import com.ruizdev.pokeapptest.data.local.dao.PokemonKeysDao
import com.ruizdev.pokeapptest.data.local.type_convertes.ListToStringConverter
import com.ruizdev.pokeapptest.data.local.type_convertes.SpriteToStringConverter
import com.ruizdev.pokeapptest.data.local.type_convertes.TypeDetailToStringConverter
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.model.Pokemon
import com.ruizdev.pokeapptest.domain.model.PokemonKeys
import com.ruizdev.pokeapptest.domain.model.TypeDetail
import com.ruizdev.pokeapptest.util.Constants.DATABASE_NAME

@Database(
    entities = [Pokemon::class, PokemonKeys::class, ApiResponsePokemonDetail::class],
    version = 1
)
@TypeConverters(SpriteToStringConverter::class, ListToStringConverter::class, TypeDetailToStringConverter::class)
abstract class PokemonDatabase : RoomDatabase() {

    companion object {

        fun create(context: Context): PokemonDatabase {
            val databaseBuilder =
                Room.databaseBuilder(context, PokemonDatabase::class.java, DATABASE_NAME)
            return databaseBuilder.build()
        }

    }

    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonKeysDao(): PokemonKeysDao
    abstract fun pokemonDetailsDao(): PokemonDetailsDao

}