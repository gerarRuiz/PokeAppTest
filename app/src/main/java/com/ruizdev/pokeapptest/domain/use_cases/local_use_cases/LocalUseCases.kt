package com.ruizdev.pokeapptest.domain.use_cases.local_use_cases

data class LocalUseCases(
    val insertPokemonDetailsUseCase: InsertPokemonDetailsUseCase,
    val readPokemonDetailsUseCase: ReadPokemonDetailsUseCase,
    val markPokemonFavoriteuseCase: MarkPokemonFavoriteuseCase
)