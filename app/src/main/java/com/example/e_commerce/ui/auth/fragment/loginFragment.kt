package com.example.e_commerce.ui.auth.fragment



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.BuildConfig.clientServerId
import com.example.e_commerce.data.datasource.datastore.UserPreferencesDataSource
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl

import com.example.e_commerce.data.repository.user.UserDataStoreRepositoryImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModel
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModelFactory
import com.example.e_commerce.ui.common.repository.ProgressDialog
import com.example.e_commerce.utils.showSnakeBarError
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {



    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userPrefs = UserDataStoreRepositoryImpl(
                UserPreferencesDataSource(
                    requireActivity()
                )), authRepository = FirebaseAuthRepositoryImpl()
        )
    }

    private var _binding: FragmentLoginBinding? = null
    private  val binding get() = _binding!!

    private val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    private val loginManager: LoginManager    by lazy {
        LoginManager.getInstance()
    }

    private val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewmodel=loginViewModel
        binding.lifecycleOwner=viewLifecycleOwner
        return _binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initViewModel()
    }


    private fun initListener() {
        binding.loginBtn.setOnClickListener {
            loginViewModel.loginWithEmailAndPassword()
        }
        binding.googleSigninBtn.setOnClickListener {
            loginWithGoogleRequest()
        }
        binding.facebookSigninBtn.setOnClickListener {
            loginWithFacebookRequest()
        }
    }

    private fun initViewModel() {

        lifecycleScope.launch{
            loginViewModel.loginState.collect{ resource ->
                when(resource)
                {
                    is Resource.Loading ->   progressDialog.show()
                    is Resource.Success ->  progressDialog.dismiss()
                    is Resource.Error   -> progressDialog.dismiss()


                }
            }
        }
    }


    private fun loginWithGoogleRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientServerId)
            .requestEmail()
            .requestProfile()
            .requestServerAuthCode(clientServerId).build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInWithGooglResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_field_msg))
            }
        }



    private fun handleSignInWithGooglResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            //logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        loginViewModel.loginWithGoogle(idToken)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun loginWithFacebookRequest() {

      signOut()

        loginManager.registerCallback(callbackManager,object :FacebookCallback<LoginResult>{

          override fun onSuccess(result: LoginResult) {

                val idToken = result.accessToken.token
                firebaseAuthWithFacebook(idToken)
            }
            override fun onCancel() {}

            override fun onError(error: FacebookException) {
                view?.showSnakeBarError(R.string.facebook_sign_in_field_msg.toString())
            }
        })

        loginManager?.logInWithReadPermissions(
            this, callbackManager , listOf("email", "public_profile")
        )


    }



    private fun signOut() {
        loginManager.logOut()

    }

//    private fun isLoggedIn(): Boolean {
//        val accessToken = AccessToken.getCurrentAccessToken()
//        return accessToken != null && !accessToken.isExpired
//    }

    private fun firebaseAuthWithFacebook(idToken: String) {
        loginViewModel.loginWithFacebook(idToken)
    }



}



