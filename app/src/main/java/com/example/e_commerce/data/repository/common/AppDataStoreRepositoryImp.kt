package com.example.e_commerce.data.repository.common


import com.example.e_commerce.data.datasource.datastore.AppPreferenceDataStore
import kotlinx.coroutines.flow.Flow


class AppDataStoreRepositoryImpl(
    private val appPreferencesDataSource: AppPreferenceDataStore
 ): AppPreferenceRepository {


    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        return appPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return appPreferencesDataSource.isUserLoggedIn
    }




}

