package com.example.e_commerce.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepositoryImp
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferencesRepository:UserPreferenceRepository,
    private val userFirestoreRepository: UserFirestoreRepository
) : ViewModel() {


    suspend fun isUserLoggedIn() = appPreferencesRepository.isUserLoggedIn()
    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(IO) {
            appPreferencesRepository.saveLoginState(b)
        }
    }

}

class UserViewModelFactory(

    private val  appPreferencesRepository: AppPreferenceRepository,
    private val  userPreferencesRepository:UserPreferenceRepository,
    private val  userFirestoreRepository: UserFirestoreRepository ):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(appPreferencesRepository,userPreferencesRepository,userFirestoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}