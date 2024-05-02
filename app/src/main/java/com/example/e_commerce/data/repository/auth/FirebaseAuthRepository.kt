package com.example.e_commerce.data.repository.auth

import com.example.e_commerce.data.models.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<String>>

}