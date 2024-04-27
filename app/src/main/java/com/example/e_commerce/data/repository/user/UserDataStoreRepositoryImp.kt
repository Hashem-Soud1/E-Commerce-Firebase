package com.example.e_commerce.data.repository.user


import android.content.Context
import androidx.datastore.preferences.core.edit
import  com.example.e_commerce.data.datasource.datastore.DataStoreKeys.IS_USER_LOGGED_IN
import com.example.e_commerce.data.datasource.datastore.DataStoreKeys.USER_ID
import com.example.e_commerce.data.datasource.datastore.UserPreferencesDataSource
import  com.example.e_commerce.data.datasource.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserDataStoreRepositoryImpl(
    private val userPreferencesDataSource: UserPreferencesDataSource
 ): UserPreferenceRepository {


    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        return userPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun saveUserID(userId: String) {
        return userPreferencesDataSource.saveUserID(userId)
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return userPreferencesDataSource.isUserLoggedIn
    }

    override suspend fun getUserID(): Flow<String?> {
        return userPreferencesDataSource.userID
    }


}

