package com.ruizdev.pokeapptest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreUseCases: DataStoreUseCases
) : ViewModel() {

    private val _sessionUUID = MutableStateFlow("")
    val sessionUUID: StateFlow<String> = _sessionUUID

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _sessionUUID.value =
                dataStoreUseCases.readSessionUUIDUseCase().stateIn(viewModelScope).value
        }
    }

    fun saveSessionUUID(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCases.saveSessionUUIDUseCase(uuid)
        }
    }
}