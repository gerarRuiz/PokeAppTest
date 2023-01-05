package com.ruizdev.pokeapptest.data.local.type_convertes

import androidx.room.TypeConverter
import com.ruizdev.pokeapptest.domain.model.TypeDetail

class TypeDetailToStringConverter {

    @TypeConverter
    fun convertTypeDetailToString(typeDetail: TypeDetail): String {
        return typeDetail.name
    }

    @TypeConverter
    fun convertStringToTypeDetail(string: String): TypeDetail {
        return TypeDetail(
            name = string
        )
    }

}