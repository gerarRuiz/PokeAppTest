package com.ruizdev.pokeapptest.data.local.type_convertes

import androidx.room.TypeConverter
import com.ruizdev.pokeapptest.domain.model.Types
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListToStringConverter {

    @TypeConverter
    fun convertListToString(list: List<Types>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun convertStringToTypeList(string: String): List<Types> {
        return if (string.isNotEmpty()) {
            Json.decodeFromString(string)
        } else {
            listOf()
        }
    }

}