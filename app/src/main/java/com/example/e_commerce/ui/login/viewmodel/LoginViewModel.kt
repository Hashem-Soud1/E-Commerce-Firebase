package com.example.e_commerce.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    val userPrefs: UserPreferenceRepository
) : ViewModel() {

    fun saveLoginState(isLoggedIn: Boolean) {
        viewModelScope.launch {


        }
    }
}