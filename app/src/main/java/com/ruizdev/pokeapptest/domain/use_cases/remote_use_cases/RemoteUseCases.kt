package com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases

data class RemoteUseCases(
    val getAllPokemon: GetAllPokemonUseCase,
    val getPokemonDetailUseCase: GetPokemonDetailUseCase
)