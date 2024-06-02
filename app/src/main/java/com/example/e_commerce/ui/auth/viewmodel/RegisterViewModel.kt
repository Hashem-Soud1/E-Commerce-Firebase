package com.example.e_commerce.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.datasource.datastore.AppPreferencesDataSource
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepositoryImpl
import com.example.e_commerce.utils.isValidEmail
import io.reactivex.annotations.SchedulerSupport.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _registerState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val registerState: SharedFlow<Resource<UserDetailsModel>> = _registerState.asSharedFlow()

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
        val name = name.value
        val email = email.value
        val password = password.value
        if (isRegisterIsValid.first()) {
            // handle register flow
            authRepository.registerWithEmailAndPassword(name, email, password).collect {
                _registerState.emit(it)
            }
        } else {
            // emit error
        }
    }

    fun signUpWithGoogle(idToken: String) = viewModelScope.launch {
        authRepository.registerWithGoogle(idToken).collect {
            _registerState.emit(it)
        }
    }

    fun registerWithFacebook(token: String) = viewModelScope.launch {
        authRepository.registerWithFacebook(token).collect {
            _registerState.emit(it)
        }
    }
}

// create viewmodel factory class
class RegisterViewModelFactory(
    private val contextValue: Context
) : ViewModelProvider.Factory {

    private val appPreferenceRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(contextValue))
    private val userPreferenceRepository = UserPreferenceRepositoryImpl(contextValue)
    private val authRepository = FirebaseAuthRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return RegisterViewModel(
                appPreferenceRepository,
                userPreferenceRepository,
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

