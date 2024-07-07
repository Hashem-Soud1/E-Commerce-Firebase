package com.example.e_commerce.data.repository.user

import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.data.models.user.CountryDetails
import com.example.e_commerce.data.models.user.UserDetailsPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences)


    suspend fun getUserId(): Flow<String>
    suspend fun updateUserId(userId: String)


    suspend fun clearUserPreferences()

    suspend fun saveUserCountry(country: CountryModel)

     fun getUserCountry(): Flow<CountryDetails>
}