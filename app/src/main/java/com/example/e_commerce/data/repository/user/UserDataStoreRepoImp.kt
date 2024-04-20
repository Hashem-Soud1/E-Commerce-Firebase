package com.example.e_commerce.data.repository.user


import android.content.Context
import androidx.datastore.preferences.core.edit
import  com.example.e_commerce.data.datasource.datastore.DataStoreKeys.IS_USER_LOGGED_IN
import com.example.e_commerce.data.datasource.datastore.DataStoreKeys.USER_ID
import  com.example.e_commerce.data.datasource.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserDataStoreRepositoryImpl(private var context: Context): UserPreferenceRepository {

    // Write to DataStore
    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    override suspend fun saveUserID(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            // Return the logged-in state, defaulting to false if not found
            preferences[IS_USER_LOGGED_IN] ?: false
        }
    }

}

