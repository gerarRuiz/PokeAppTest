package com.ruizdev.pokeapptest.presentation.fragments.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases.RemoteUseCases
import com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases.DataStoreUseCases
import com.ruizdev.pokeapptest.domain.use_cases.local_use_cases.LocalUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val remoteUseCases: RemoteUseCases,
    private val dataStoreUseCases: DataStoreUseCases,
    private val localUseCases: LocalUseCases
) : ViewModel() {

    private val _backgroundColor = MutableStateFlow(0)
    val backgroundColor: StateFlow<Int> = _backgroundColor

    private val _textColor = MutableStateFlow(0)
    val textColor: StateFlow<Int> = _textColor

    val getAllPokemon = remoteUseCases.getAllPokemon()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _backgroundColor.value =
                dataStoreUseCases.readBackgroundColorUseCase().stateIn(viewModelScope).value
            _textColor.value =
                dataStoreUseCases.readTextColorUseCase().stateIn(viewModelScope).value
        }
    }

    fun markFavorite(pokemonId: Long, favorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            localUseCases.markPokemonFavoriteuseCase(pokemonId, favorite)
        }
    }

}