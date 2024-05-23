package com.example.e_commerce.data.repository.user

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserFirestoreRepositoryImp (
    private val firestore: FirebaseFirestore=FirebaseFirestore.getInstance()
): UserFirestoreRepository {
    override suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>{
        return callbackFlow {
            trySend(Resource.Loading())
            val documentPath = "users/$userId"
            val doc = firestore.document(documentPath)
            val listener = doc.addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(Exception(error.message)))
                    close(error)
                    return@addSnapshotListener
                }
                value?.toObject(UserDetailsModel::class.java)?.let {
                    trySend(Resource.Success(it))
                } ?: run {
                    close(UserNotFoundException("User not found"))
                }
            }
            awaitClose {
                listener.remove()
            }
        }


    }

    class UserNotFoundException(message: String) : Exception(message) {}

}

