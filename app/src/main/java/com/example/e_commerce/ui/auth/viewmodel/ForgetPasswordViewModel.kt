package com.example.e_commerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow

class ForgetPasswordViewModel(val authRepository: FirebaseAuthRepository) : ViewModel() {


    val email = MutableStateFlow("")

    fun sendUpdatePasswordEmail() {

    }
}

class ForgetPasswordViewModelFactory(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepositoryImpl()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ForgetPasswordViewModel(
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}