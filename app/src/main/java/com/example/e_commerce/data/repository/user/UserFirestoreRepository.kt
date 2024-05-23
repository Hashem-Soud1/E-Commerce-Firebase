package com.example.e_commerce.data.repository.user

import com.example.e_commerce.ui.common.model.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
    suspend fun getUserDetails(userId: String): Flow<UserDetailsModel>
}