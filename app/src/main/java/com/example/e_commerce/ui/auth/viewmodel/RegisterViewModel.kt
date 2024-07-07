package com.example.e_commerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.RegisterRequestModel
import com.example.e_commerce.data.models.auth.RegisterResponseModel
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class RegisterViewModel @Inject constructor(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _registerState = MutableSharedFlow<Resource<RegisterResponseModel>>()
    val registerState: SharedFlow<Resource<RegisterResponseModel>> = _registerState.asSharedFlow()

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val isRegisterIsValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        email.isValidEmail() && password.length >= 6 && name.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    }

    fun registerWithEmailAndPassword() = viewModelScope.launch(Dispatchers.IO) {

//        val registerRequestModel = RegisterRequestModel(
//            fullName = name.value,
//            email = email.value,
//            password = password.value
//        )
//        if (isRegisterIsValid.first()) {
//            // handle register flow
//            authRepository.registerWithEmailAndPasswordWithApi(registerRequestModel).collect {
//                _registerState.emit(it)
//            }
//        } else {
//            // emit error
//        }
    }

    fun signUpWithGoogle(idToken: String) = viewModelScope.launch {
//        authRepository.registerWithGoogle(idToken).collect {
//            _registerState.emit(it)
//        }
    }

    fun registerWithFacebook(token: String) = viewModelScope.launch {
//        authRepository.registerWithFacebook(token).collect {
//            _registerState.emit(it)
//        }
    }
}

