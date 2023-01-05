package com.ruizdev.pokeapptest.domain.use_cases.local_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository

class MarkPokemonFavoriteuseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(pokemonId: Long, favorite: Boolean) {
        repository.markFavorite(pokemonId, favorite)
    }

}