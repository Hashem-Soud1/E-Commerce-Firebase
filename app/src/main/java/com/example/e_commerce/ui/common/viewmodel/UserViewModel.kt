package com.example.e_commerce.ui.common.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.data.datasource.datastore.AppPreferencesDataSource
import com.example.e_commerce.data.models.Resource

import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepositoryImp
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepositoryImpl
import com.example.e_commerce.domain.toUserDetailsModel
import com.example.e_commerce.domain.toUserDetailsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.mapLatest as mapLatest1
@HiltViewModel
class UserViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferencesRepository:UserPreferenceRepository,
    private val userFirestoreRepository: UserFirestoreRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {


    var logoutState=MutableSharedFlow<Resource<Unit>>()
    // load user data in state flow inside view model  scope
    val userDetailsState = getUserDetails()
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = null)

    init {
        listenToUserDetails()
    }
    // load user data flow
    // we can use this to get user data in the view in main thread so we do not want to wait the data from state
    // note that this flow block the main thread while you get the data every time you call it
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserDetails() =
        userPreferencesRepository.getUserDetails().mapLatest1 { it.toUserDetailsModel() }


    private fun listenToUserDetails() = viewModelScope.launch {
        val userId = userPreferencesRepository.getUserId().first()
        if (userId.isEmpty()) return@launch
        userFirestoreRepository.getUserDetails(userId).onEach { resource ->
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
        logoutState.emit(Resource.Loading())
        authRepository.logout()
        appPreferencesRepository.saveLoginState(false)
        userPreferencesRepository.clearUserPreferences()
        logoutState.emit(Resource.Success(Unit))
    }
    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(IO) {
            appPreferencesRepository.saveLoginState(b)
        }
    }

}

