package com.ruizdev.pokeapptest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponsePokemon(
    val count: Int,
    val next: String? = null,
    val previous: String ? = null,
    val results: List<Pokemon>
)
