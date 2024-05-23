package com.example.e_commerce.data.repository.user

import com.example.e_commerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
    suspend fun getUserDetails(userId: String): Flow<UserDetailsModel>
}