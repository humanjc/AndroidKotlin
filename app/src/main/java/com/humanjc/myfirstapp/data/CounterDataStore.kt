package com.humanjc.myfirstapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "counter_prefs")

class CounterDataStore(private val context: Context) {

    companion object {
        private val COUNT_KEY = intPreferencesKey("count")
        private val MAX_COUNT_KEY = intPreferencesKey("max_count")
        private val HISTORY_KEY = stringPreferencesKey("history")
        private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    }

    val countFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[COUNT_KEY] ?: 0
    }

    val maxCountFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[MAX_COUNT_KEY] ?: 10
    }

    val historyFlow: Flow<List<String>> = context.dataStore.data.map { preferences ->
        val historyString = preferences[HISTORY_KEY] ?: ""
        if (historyString.isEmpty()) emptyList() else historyString.split("|")
    }

    val isDarkModeFlow: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE_KEY]
    }

    suspend fun saveCounterData(count: Int, maxCount: Int, history: List<String>, isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COUNT_KEY] = count
            preferences[MAX_COUNT_KEY] = maxCount
            preferences[HISTORY_KEY] = history.joinToString("|")
            preferences[IS_DARK_MODE_KEY] = isDarkMode
        }
    }

    suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
