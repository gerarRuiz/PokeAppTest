package com.ruizdev.pokeapptest.util.network

sealed class UIState<out T> {

    object InitialState: UIState<Nothing>()
    data class Success<out T>(val data: T): UIState<T>()
    data class Error<out T>(val error: String = ""): UIState<T>()
    data class Loading<out T>(val status: Boolean = false): UIState<T>()

}
