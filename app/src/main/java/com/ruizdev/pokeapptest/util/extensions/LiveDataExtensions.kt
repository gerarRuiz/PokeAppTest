package com.ruizdev.pokeapptest.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/** Updates the MutableLiveData.value atomically using the specified function of its value. **/
inline fun <T> MutableLiveData<T>.update(function: (T?) -> T?) {
    val newValue = function(value)
    this.value = newValue
}

/** Represents this mutable live data as live data. **/
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}