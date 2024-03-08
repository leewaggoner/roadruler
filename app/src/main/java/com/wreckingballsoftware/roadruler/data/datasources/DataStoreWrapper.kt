package com.wreckingballsoftware.roadruler.data.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataStoreWrapper @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKey {
        val USER_ID = stringPreferencesKey("UserId")
    }

    suspend fun putUserId(userId: String) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_ID] = userId
        }
    }

    suspend fun getUserId(default: String): String = withContext(Dispatchers.IO) {
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[PreferencesKey.USER_ID] ?: default
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it.clear()
        }
    }
}