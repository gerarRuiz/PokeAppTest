package com.ruizdev.pokeapptest.presentation.fragments.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.use_cases.local_use_cases.LocalUseCases
import com.ruizdev.pokeapptest.domain.use_cases.remote_use_cases.RemoteUseCases
import com.ruizdev.pokeapptest.util.extensions.asLiveData
import com.ruizdev.pokeapptest.util.extensions.update
import com.ruizdev.pokeapptest.util.network.Result
import com.ruizdev.pokeapptest.util.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val remoteUseCases: RemoteUseCases,
    private val localUseCases: LocalUseCases
) : ViewModel() {

    private val _detailPokemonState: MutableLiveData<UIState<ApiResponsePokemonDetail>?> =
        MutableLiveData(null)
    val detailPokemonState = _detailPokemonState.asLiveData()

    private val _readPokemonDetails: MutableLiveData<ApiResponsePokemonDetail?> =
        MutableLiveData(null)
    val readPokemonDetails = _readPokemonDetails.asLiveData()

    /**
     * Local
     */

    fun getPokemonDetailsLocal(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _readPokemonDetails.postValue(localUseCases.readPokemonDetailsUseCase(pokemonId))
        }
    }

    fun markFavorite(pokemonId: Long, favorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            localUseCases.markPokemonFavoriteuseCase(pokemonId, favorite)
        }
    }

    /**
     * Remote
     */

    fun getPokemonDetails(pokemonId: Int) = viewModelScope.launch {

        _detailPokemonState.update { UIState.Loading(status = true) }
        val response = remoteUseCases.getPokemonDetailUseCase(pokemonId)
        _detailPokemonState.update { UIState.Loading(status = false) }
        when (response) {
            is Result.Success -> {
                localUseCases.insertPokemonDetailsUseCase(response.data)
                _detailPokemonState.update { UIState.Success(response.data) }
            }
            is Result.Error -> _detailPokemonState.update { UIState.Error(response.error) }
        }
        _detailPokemonState.update { UIState.InitialState }

    }

}