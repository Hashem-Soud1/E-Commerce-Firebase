package com.example.e_commerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.utils.isValidEmail
import com.example.e_commerce.utils.isValidPassword

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val  appPreferenceRepository  : AppPreferenceRepository,
    private  val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {


//use for Login with email and password
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginIsValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.isValidPassword()
    }

    //use all way of Login
    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState :SharedFlow<Resource<String>> = _loginState.asSharedFlow()


    fun loginWithEmailAndPassword() {
    viewModelScope.launch {

//      val email    = email.value
//      val password = password.value

       if (isLoginIsValid.first()) {
                authRepository.loginWithEmailAndPassword(email.value, password.value).onEach { resource ->

          when (resource) {
           is Resource.Loading -> _loginState.emit(Resource.Loading())
           is Resource.Success -> {
               savePrefrencesData(resource.data!!)
               _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
           }
           is Resource.Error ->   _loginState.emit(Resource.Error(resource.exception ?: Exception("Unknown error")))
          }
                }.launchIn(viewModelScope)

            } else
                _loginState.emit(Resource.Error(Exception("Invalid email or password")))

        }
    }

    private suspend fun  savePrefrencesData(userId: String) {

            appPreferenceRepository.saveLoginState(true)
            userPreferenceRepository.updateUserId(userId)
        }

    fun loginWithGoogle(idToken: String)=
        viewModelScope.launch {
       authRepository.loginWithGoogle(idToken).onEach { resource ->

        when(resource)
        {
            is Resource.Success ->{
                savePrefrencesData(resource.data!!)
                _loginState.emit(Resource.Success(resource.data?:"Empty"))
            }

            else -> _loginState.emit(resource)

        }
              }.launchIn(viewModelScope)
        }

    fun loginWithFacebook(idToken: String)=
        viewModelScope.launch {
            authRepository.loginWithGoogle(idToken).onEach { resource ->

                when(resource)
                {
                    is Resource.Success -> {
                        savePrefrencesData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data?:"Empty"))
                    }

                    else -> _loginState.emit(resource)

                }
            }.launchIn(viewModelScope)
        }


}

class LoginViewModelFactory(
    private val  appPreferenceRepository  : AppPreferenceRepository,
    private  val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(appPreferenceRepository,userPreferenceRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}