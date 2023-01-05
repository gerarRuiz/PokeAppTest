package com.ruizdev.pokeapptest.util

object Constants {

    const val BASE_URL = "https://pokeapi.co/api/v2/"
    const val BASE_URL_SPRITE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

    const val CONNECTION_TIMEOUT = 15L
    const val READ_TIMEOUT = 15L

    const val SPLASH_TIMEOUT = 3500L

    const val LOCATION_INTERVAL = 200000L
    const val LOCATION_INTERVAL_FASTEST = 150000L

    const val ACTION_START_LOCATION_SERVICE = "start_location_service"
    const val ACTION_STOP_LOCATION_SERVICE = "stop_location_service"

    const val NOTIFICATION_CHANNEL_ID = "12345"
    const val NOTIFICATION_CHANNEL_NAME = "Locations"
    const val NOTIFICATION_ID = 12345

    const val COLL_LOCATIONS = "locations"
    const val COLL_LOCATION = "location"

    /***
     * Database
     */

    const val DATABASE_NAME = "pokemon_database"
    const val POKEMON_DB_TABLE = "pokemon_db_table"
    const val POKEMON_KEYS_DB_TABLE = "pokemon_keys_table"
    const val POKEMON_DETAIL_DB_TABLE = "pokemon_detail_db_table"

    /**
     * Preferences
     */

    const val PREFERENCES_NAME = "pokemon_preferences"
    const val PREFERENCE_AVATAR_URL = "avatar_url"
    const val PREFERENCE_WORD = "word"
    const val PREFERENCE_BACKGROUND_COLOR = "background_color"
    const val PREFERENCE_TEXT_COLOR = "text_color"
    const val PREFERENCE_SESSION_UUID = "uuid"

}