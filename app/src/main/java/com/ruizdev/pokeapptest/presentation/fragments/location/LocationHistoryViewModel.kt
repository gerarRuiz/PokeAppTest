package com.ruiz.emovie.presentation.fragments.location

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
class LocationHistoryViewModel @Inject constructor(
    private val dataStoreUseCases: DataStoreUseCases
): ViewModel() {

    private val _sessionId = MutableStateFlow("")
    val sessionId: StateFlow<String> = _sessionId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _sessionId.value = dataStoreUseCases.readSessionUUIDUseCase().stateIn(viewModelScope).value
        }
    }

}