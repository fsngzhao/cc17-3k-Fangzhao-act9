package com.example.flightsearch

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.stringPreferencesKey

class PreferencesManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("user_preferences")


    private val SEARCH_TEXT_KEY = stringPreferencesKey("search_text")
    private val FAVORITES_KEY = stringSetPreferencesKey("favorites")

    fun saveSearchText(text: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[SEARCH_TEXT_KEY] = text
            }
        }
    }

    fun getSearchText(callback: (String) -> Unit) {
        runBlocking {
            val preferences = context.dataStore.data.first()
            callback(preferences[SEARCH_TEXT_KEY] ?: "")
        }
    }

    fun addFavorite(flight: Flight) {
        runBlocking {
            context.dataStore.edit { preferences ->
                val currentFavorites = preferences[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
                currentFavorites.add("${flight.departure},${flight.arrival},${flight.departureName},${flight.arrivalName}")
                preferences[FAVORITES_KEY] = currentFavorites
            }
        }
    }

    fun getFavorites(): List<Flight> {
        return runBlocking {
            val preferences = context.dataStore.data.first()
            val favorites = preferences[FAVORITES_KEY] ?: emptySet()
            favorites.map {
                val parts = it.split(",")
                Flight(parts[0], parts[1], parts[2], parts[3])
            }
        }
    }

    fun clearFavorites() {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[FAVORITES_KEY] = emptySet()
            }
        }
    }
}
