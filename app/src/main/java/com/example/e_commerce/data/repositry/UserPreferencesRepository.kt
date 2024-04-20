package com.example.e_commerce.data.repositry


import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.e_commerce.data.datasource.datastore.DataStoreKeys.IS_USER_LOGGED_IN
import com.example.e_commerce.data.datasource.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferencesRepository(private val context: Context) {

    // Read from DataStore
    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Return the logged-in state, defaulting to false if not found
            preferences[IS_USER_LOGGED_IN] ?: false
        }

    // Write to DataStore
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }
}


