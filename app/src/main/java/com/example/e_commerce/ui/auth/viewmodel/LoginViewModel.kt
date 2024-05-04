package com.example.e_commerce.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.utils.isValidEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {



    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginIsValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6 }

    val loginState = MutableSharedFlow<Resource<String>>()


fun login() {
    viewModelScope.launch {

      val email    = email.value
      val password = password.value

       if (isLoginIsValid.first()) {
                authRepository.loginWithEmailAndPassword(email, password).onEach { resource ->

          when (resource) {
           is Resource.Loading -> loginState.emit(Resource.Loading())
           is Resource.Success -> loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
           is Resource.Error ->  loginState.emit( Resource.Error(resource.exception ?: Exception("Unknown error")))
          }
                }.launchIn(viewModelScope)

            } else
                loginState.emit(Resource.Error(Exception("Invalid email or password")))

        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

class LoginViewModelFactory(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(userPrefs, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}