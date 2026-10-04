package com.arsenalsimulator.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore("settings")

class SettingsViewModel(private val appContext: Context) : ViewModel() {
    private val KEY = booleanPreferencesKey("dark_mode")
    val darkMode = appContext.dataStore.data.map { it[KEY] ?: false }

    fun setDark(enabled: Boolean) {
        viewModelScope.launch { appContext.dataStore.edit { it[KEY] = enabled } }
    }

    companion object {
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(context.applicationContext) as T
            }
        }
    }
}
