package com.taskmanagerapp.taskmanager.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    // Read dark mode preference
    val darkModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false  // Default is false (light mode)
    }

    // Save dark mode preference
    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }
}
