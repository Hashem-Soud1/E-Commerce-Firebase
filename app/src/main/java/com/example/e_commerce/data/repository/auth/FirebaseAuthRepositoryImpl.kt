package com.example.e_commerce.data.repository.auth

import android.util.Log
import com.example.e_commerce.data.data_source.networking.CloudFunctionAPI
import com.example.e_commerce.data.data_source.networking.handleErrorResponse
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.RegisterRequestModel
import com.example.e_commerce.data.models.auth.RegisterResponseModel
import com.example.e_commerce.data.models.user.AuthProvider
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.LoginException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth ,
    private val firestore: FirebaseFirestore,
    private val cloudFunctionAPI: CloudFunctionAPI
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
     override suspend fun loginWithFacebook(token: String)
     = login(
         AuthProvider.FACEBOOK,
         signInRequest =  {
         val credential = FacebookAuthProvider.getCredential(token)
         auth.signInWithCredential(credential).await()
     } )



     private  suspend fun login(
        authProvider: AuthProvider,
        signInRequest: suspend () -> AuthResult
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())
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

     override suspend fun logout() {
         auth.signOut()
     }

     override suspend fun registerWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>> {
         return flow {
             try {
                 emit(Resource.Loading())
                 // perform firebase auth sign up request
                 val credential = GoogleAuthProvider.getCredential(idToken, null)
                 val authResult = auth.signInWithCredential(credential).await()
                 val userId = authResult.user?.uid

                 if (userId == null) {
                     val msg = "Sign up UserID not found"
                     logAuthIssueToCrashlytics(msg, AuthProvider.GOOGLE.name)
                     emit(Resource.Error(Exception(msg)))
                     return@flow
                 }

                 // create user details object
                 val userDetails = UserDetailsModel(
                     id = userId,
                     name = authResult.user?.displayName ?: "",
                     email = authResult.user?.email ?: "",
                     createdAt = System.currentTimeMillis()
                 )
                 // save user details to firestore
                 firestore.collection("users").document(userId).set(userDetails).await()
                 emit(Resource.Success(userDetails))
             } catch (e: Exception) {
                 logAuthIssueToCrashlytics(
                     e.message ?: "Unknown error from exception = ${e::class.java}",
                     AuthProvider.GOOGLE.name
                 )
                 emit(Resource.Error(e)) // Emit error
             }
         }
     }

     override suspend fun registerWithFacebook(token: String): Flow<Resource<UserDetailsModel>> {
         return flow {
             try {
                 emit(Resource.Loading())
                 // perform firebase auth sign up request
                 val credential = FacebookAuthProvider.getCredential(token)
                 val authResult = auth.signInWithCredential(credential).await()
                 val userId = authResult.user?.uid

                 if (userId == null) {
                     val msg = "Sign up UserID not found"
                     logAuthIssueToCrashlytics(msg, AuthProvider.FACEBOOK.name)
                     emit(Resource.Error(Exception(msg)))
                     return@flow
                 }

                 // create user details object
                 val userDetails = UserDetailsModel(
                     id = userId,
                     name = authResult.user?.displayName ?: "",
                     email = authResult.user?.email ?: "",
                     createdAt = System.currentTimeMillis()
                 )
                 // save user details to firestore
                 firestore.collection("users").document(userId).set(userDetails).await()
                 emit(Resource.Success(userDetails))
             } catch (e: Exception) {
                 logAuthIssueToCrashlytics(
                     e.message ?: "Unknown error from exception = ${e::class.java}",
                     AuthProvider.FACEBOOK.name
                 )
                 emit(Resource.Error(e)) // Emit error
             }
         }
     }

     override suspend fun registerWithEmailAndPassword(
         name: String, email: String, password: String
     ): Flow<Resource<UserDetailsModel>> {

         return flow {
             try {
                 emit(Resource.Loading())
                 // perform firebase auth sign up request
                 val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                 val userId = authResult.user?.uid

                 if (userId == null) {
                     val msg = "Sign up UserID not found"
                     logAuthIssueToCrashlytics(msg, AuthProvider.EMAIL.name)
                     emit(Resource.Error(Exception(msg)))
                     return@flow
                 }

                 // create user details object
                 val userDetails = UserDetailsModel(
                     id = userId, name = name, email = email, createdAt = System.currentTimeMillis()
                 )
                 // save user details to firestore
                 firestore.collection("users").document(userId).set(userDetails).await()
                 authResult?.user?.sendEmailVerification()?.await()
                 emit(Resource.Success(userDetails))
             } catch (e: Exception) {
                 logAuthIssueToCrashlytics(
                     e.message ?: "Unknown error from exception = ${e::class.java}",
                     AuthProvider.EMAIL.name
                 )
                 emit(Resource.Error(e)) // Emit error
             }
         }
     }

     private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
         CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
             msg,
             CrashlyticsUtils.LOGIN_KEY to msg,
             CrashlyticsUtils.LOGIN_PROVIDER to provider,
         )
     }

     override suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>> {
         return flow {
             try {
                 emit(Resource.Loading())
                 auth.sendPasswordResetEmail(email).await()
                 emit(Resource.Success("Password reset email sent"))
             } catch (e: Exception) {
                 emit(Resource.Error(e)) // Emit error
             }
         }
     }

    override suspend fun registerWithEmailAndPasswordWithApi(registerRequestModel: RegisterRequestModel): Flow<Resource<RegisterResponseModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = cloudFunctionAPI.registerUser(registerRequestModel)
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    registerResponse?.data?.let {
                        emit(Resource.Success(it))
                    } ?: run {
                        emit(Resource.Error(Exception(registerResponse?.message)))
                    }
                } else {
                    Log.d(
                        TAG,
                        "registerEmailAndPasswordWithAPI: Error registering user = ${response.errorBody()}"
                    )
                    emit(Resource.Error(Exception(handleErrorResponse(response.errorBody()!!.charStream()))))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }



    companion object {
        private const val TAG = "FirebaseAuthRepositoryI"
    }
}