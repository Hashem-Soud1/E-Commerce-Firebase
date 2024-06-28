package com.example.e_commerce.data.repository.user

import android.app.Application
import android.util.Log
import com.example.e_commerce.data.data_source.data_store.userDetailsDataStore
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.data.models.user.CountryDetails
import com.example.e_commerce.data.models.user.UserDetailsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepositoryImpl @Inject constructor(private val context: Application)
    : UserPreferenceRepository {
    override fun getUserDetails(): Flow<UserDetailsPreferences> {
        return context.userDetailsDataStore.data
    }

    override suspend fun updateUserId(userId: String) {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setId(userId).build()
        }
    }

    override suspend fun getUserId(): Flow<String> {
        return context.userDetailsDataStore.data.map { it.id }
    }

    override suspend fun clearUserPreferences() {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    override suspend fun saveUserCountry(country: CountryModel) {
        val countryData = CountryDetails.newBuilder().setId(country.id)
            .setName(country.name).setCurrnecy(country.currnecy)
            .build()

        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setCountry(countryData).build()
        }

    }

    override suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences) {
        context.userDetailsDataStore.updateData { userDetailsPreferences }
    }

    override fun getUserCountry(): Flow<CountryDetails> {
        return context.userDetailsDataStore.data.map { it.country }
        }
    }
