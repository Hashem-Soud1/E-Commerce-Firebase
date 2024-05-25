package com.example.e_commerce.data.repository.auth

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.AuthProvider
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

 class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FirebaseAuthRepository {

     // Example usage for email and password login
     override suspend fun loginWithEmailAndPassword(
         email: String, password: String
     ) = login(AuthProvider.EMAIL) { auth.signInWithEmailAndPassword(email, password).await() }


     // Example usage for Google login
     override suspend fun loginWithGoogle(idToken: String) = login(AuthProvider.GOOGLE) {
             val credential = GoogleAuthProvider.getCredential(idToken, null)
             auth.signInWithCredential(credential).await()
         }


     // Example usage for Facebook login
     override suspend fun loginWithFacebook(token: String) = login(AuthProvider.FACEBOOK) {
         val credential = FacebookAuthProvider.getCredential(token)
         auth.signInWithCredential(credential).await()
     }



     override suspend fun logout() {
         auth.signOut()
     }

     private  suspend fun login(
        authProvider: AuthProvider,
        signInRequest: suspend () -> AuthResult
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
           // emit(Resource.Loading())
            val authResult = signInRequest() // Invoke the passed lambda action to perform login
            val userId = authResult.user?.uid!!
            val userDoc = firestore.collection("users").document(userId).get().await()
            userDoc?.let {
                val userDetails = it.toObject(UserDetailsModel::class.java)
                emit(Resource.Success(userDetails!!))
            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e)) // Emit error
        }
    }


    companion object {
        private const val TAG = "FirebaseAuthRepositoryI"
    }
}