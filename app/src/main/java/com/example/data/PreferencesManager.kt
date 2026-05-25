package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "huella_segura_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        val PROFILE_KEY = stringPreferencesKey("user_profile")
        val THEME_KEY = stringPreferencesKey("app_theme")
    }

    val profileFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PROFILE_KEY]
    }

    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "system"
    }

    suspend fun saveProfile(profile: String?) {
        context.dataStore.edit { preferences ->
            if (profile == null) {
                preferences.remove(PROFILE_KEY)
            } else {
                preferences[PROFILE_KEY] = profile
            }
        }
    }

    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.remove(PROFILE_KEY)
            // Leave theme preferred status intact or set to default,
            // but the prompt says: "borra: el progreso de los juegos (puntajes guardados) y el último perfil seleccionado."
            // So we just clear the last selected profile. We don't necessarily clear theme unless desired,
            // but we can clear PROFILE_KEY specifically or preferences.clear().
            // Let's clear the profile specifically, or full reset. Let's clear PROFILE_KEY and keep theme as-is, or clear all.
            // Let's remove the PROFILE_KEY so they return to the selection page, which conforms perfectly.
        }
    }
}
