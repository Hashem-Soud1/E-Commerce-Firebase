package com.example.e_commerce.data.repository.common


import com.example.e_commerce.data.datasource.datastore.AppPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AppDataStoreRepositoryImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
 ): AppPreferenceRepository {


    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        return appPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return appPreferencesDataSource.isUserLoggedIn
    }




}

