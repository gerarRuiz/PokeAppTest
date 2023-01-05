package com.ruizdev.pokeapptest.domain.model

import com.google.firebase.firestore.Exclude

data class LocationEvent(
    @get:Exclude var id: String? = null,
    var latitude: String = "",
    var longitude: String = "",
    var address: String = "",
    var city: String = "",
    var country: String = ""
)
