package com.example.e_commerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val userPrefs: UserPreferenceRepository,
    val authFirebase:FirebaseAuthRepository
) : ViewModel() {

    val email    =MutableStateFlow("")
    val password =MutableStateFlow("")

}

class LoginViewModelFactory(
    private val userPrefs: UserPreferenceRepository,
    private val authFirebase:FirebaseAuthRepository

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(userPrefs,authFirebase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}