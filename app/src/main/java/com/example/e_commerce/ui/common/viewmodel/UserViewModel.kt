package com.example.e_commerce.ui.common.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.data.datasource.datastore.AppPreferenceDataStore
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.toUserDetailsPreferences
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepositoryImp
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepositoryImpl
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferencesRepository:UserPreferenceRepository,
    private val userFirestoreRepository: UserFirestoreRepository
) : ViewModel() {

    // load user data in state flow inside view model  scope
    val userPrefsState = userPreferencesRepository.getUserDetails()
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = null)

    init {
        listenToUserDetails()
    }
    // load user data flow
    // we can use this to get user data in the view in main thread so we do not want to wait the data from state
    // note that this flow block the main thread while you get the data every time you call it
    fun getUserPrefsDetails() = userPreferencesRepository.getUserDetails()


    fun listenToUserDetails() = viewModelScope.launch {
        val userId = userPreferencesRepository.getUserId().first()
        if (userId.isEmpty()) return@launch
        userFirestoreRepository.getUserDetails(userId).collect { resource ->
            when (resource) {
                is Resource.Success -> {

                    resource.data?.let {
                        userPreferencesRepository.updateUserDetails(it.toUserDetailsPreferences())
                    }
                }

                else -> {
                    // Do nothing
                }
            }
        }
    }
    suspend fun isUserLoggedIn() = appPreferencesRepository.isUserLoggedIn()
    suspend fun logout() {
        appPreferencesRepository.saveLoginState(false)
        userPreferencesRepository.clearUserPreferences()
    }
    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(IO) {
            appPreferencesRepository.saveLoginState(b)
        }
    }

}

class UserViewModelFactory(
    private  val context: Context,
): ViewModelProvider.Factory {
    private val appPreferencesRepository =
        AppDataStoreRepositoryImpl(AppPreferenceDataStore(context))

    private val userPreferencesRepository = UserPreferenceRepositoryImpl(context)
    private val userFirestoreRepository = UserFirestoreRepositoryImp()
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(appPreferencesRepository,userPreferencesRepository,userFirestoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}