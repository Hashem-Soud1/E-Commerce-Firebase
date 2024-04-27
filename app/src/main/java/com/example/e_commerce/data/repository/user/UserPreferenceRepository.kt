package com.example.e_commerce.data.repository.user

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {

    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun saveUserID(userId: String)
    suspend fun getUserID():Flow<String?>
    suspend fun isUserLoggedIn(): Flow<Boolean>
}

