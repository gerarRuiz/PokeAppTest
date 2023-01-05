package com.ruizdev.pokeapptest.data.local.type_convertes

import androidx.room.TypeConverter
import com.ruizdev.pokeapptest.domain.model.Sprites

class SpriteToStringConverter {

    @TypeConverter
    fun convertSpriteToString(sprites: Sprites): String {
        return sprites.front_default
    }

    @TypeConverter
    fun convertStringToSprite(string: String): Sprites {
        return Sprites(
            front_default = string
        )
    }

}