package com.example.e_commerce.data.repository.auth

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<UserDetailsModel>>
    suspend fun loginWithGoogle(
       idToken: String
    ): Flow<Resource<UserDetailsModel>>
    suspend fun loginWithFacebook(
       idToken: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun logout()

    suspend fun registerWithEmailAndPassword(
        name: String, email: String, password: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun registerWithGoogle(
        idToken: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun registerWithFacebook(token: String): Flow<Resource<UserDetailsModel>>
 suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>>

}