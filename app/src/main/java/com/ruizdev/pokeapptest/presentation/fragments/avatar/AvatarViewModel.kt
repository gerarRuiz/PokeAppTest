package com.ruizdev.pokeapptest.presentation.fragments.avatar

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
class AvatarViewModel @Inject constructor(
    private val dataStoreUseCases: DataStoreUseCases
) : ViewModel() {

    private val _avatarUrl = MutableStateFlow("")
    val avatarUrl: StateFlow<String> = _avatarUrl

    private val _word = MutableStateFlow("")
    val word: StateFlow<String> = _word

    private val _backgroundColor = MutableStateFlow(0)
    val backgroundColor: StateFlow<Int> = _backgroundColor

    private val _textColor = MutableStateFlow(0)
    val textColor: StateFlow<Int> = _textColor

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _avatarUrl.value =
                dataStoreUseCases.readAvatarUrlUseCase().stateIn(viewModelScope).value
            _word.value = dataStoreUseCases.readWordUseCase().stateIn(viewModelScope).value
            _backgroundColor.value =
                dataStoreUseCases.readBackgroundColorUseCase().stateIn(viewModelScope).value
            _textColor.value =
                dataStoreUseCases.readTextColorUseCase().stateIn(viewModelScope).value
        }
    }

    fun saveAvatarUrl(avatarUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCases.saveAvatarUrlUseCase(avatarUrl)
        }
    }

    fun saveWord(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCases.saveWordUseCase(word)
        }
    }

    fun saveBackgroundColor(backgroundColor: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCases.saveBackgroundColorUseCase(backgroundColor)
        }
    }

    fun saveTextColor(textColor: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCases.saveTextColorUseCase(textColor)
        }
    }

}