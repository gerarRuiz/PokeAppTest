package com.ruizdev.pokeapptest.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ruizdev.pokeapptest.domain.repository.DataStoreOperations
import com.ruizdev.pokeapptest.util.Constants.PREFERENCES_NAME
import com.ruizdev.pokeapptest.util.Constants.PREFERENCE_AVATAR_URL
import com.ruizdev.pokeapptest.util.Constants.PREFERENCE_BACKGROUND_COLOR
import com.ruizdev.pokeapptest.util.Constants.PREFERENCE_SESSION_UUID
import com.ruizdev.pokeapptest.util.Constants.PREFERENCE_TEXT_COLOR
import com.ruizdev.pokeapptest.util.Constants.PREFERENCE_WORD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreOperationsImpl(
    context: Context
) : DataStoreOperations {

    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    private object PreferencesKey {
        val avatarUrl = stringPreferencesKey(name = PREFERENCE_AVATAR_URL)
        val word = stringPreferencesKey(name = PREFERENCE_WORD)
        val backgroundColor = intPreferencesKey(name = PREFERENCE_BACKGROUND_COLOR)
        val textColor = intPreferencesKey(name = PREFERENCE_TEXT_COLOR)
        val sessionUUID = stringPreferencesKey(name = PREFERENCE_SESSION_UUID)
    }

    override suspend fun saveAvatarUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.avatarUrl] = url
        }
    }

    override fun readAvatarUrl(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val avatarUrlState = preferences[PreferencesKey.avatarUrl] ?: ""
                avatarUrlState
            }
    }

    override suspend fun saveWord(word: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.word] = word
        }
    }

    override fun readWord(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val wordState = preferences[PreferencesKey.word] ?: ""
                wordState
            }
    }

    override suspend fun saveBackgroundColor(color: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.backgroundColor] = color
        }
    }

    override fun readBackgroundColor(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val backgroundColorState = preferences[PreferencesKey.backgroundColor] ?: -1
                backgroundColorState
            }
    }

    override suspend fun saveTextColor(color: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.textColor] = color
        }
    }

    override fun readTextColor(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val textColorState = preferences[PreferencesKey.textColor] ?: -1
                textColorState
            }
    }

    override suspend fun saveSessionUUID(uuid: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.sessionUUID] = uuid
        }
    }

    override fun readSessionUUID(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uuidState = preferences[PreferencesKey.sessionUUID] ?: ""
                uuidState
            }
    }
}