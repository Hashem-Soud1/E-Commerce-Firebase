package com.example.e_commerce.ui.auth.fragment



import android.content.Intent

import android.util.Log

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.auth.getGoogleRequestIntent
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModel
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModelFactory
import com.example.e_commerce.ui.home.MainActivity
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.LoginException
import com.example.e_commerce.utils.showSnakeBarError
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    override val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(contextValue = requireContext())
    }

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override fun init() {

        initListeners()
        initViewModel()


    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.loginState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        goToHome()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)

                        view?.showSnakeBarError(msg)
                    }
                }
            }
        }
    }

    private fun goToHome() {
        requireActivity().startActivity(Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        requireActivity().finish()
    }

    private fun initListeners() {
        binding.loginBtn.setOnClickListener {
            viewModel.loginWithEmailAndPassword()
        }
        binding.googleSigninBtn.setOnClickListener {
            loginWithGoogleRequest()
        }
        binding.facebookSigninBtn.setOnClickListener {
            loginWithFacebook()
        }
        binding.forgotPasswordTv.setOnClickListener {
            val forgetPasswordFragment = ForgetPasswordFragment()
            forgetPasswordFragment.show(parentFragmentManager, "forget-password")
        }
        binding.registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

    // ActivityResultLauncher for the sign-in intent
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_field_msg))
            }
        }

    private fun loginWithGoogleRequest() {
        val signInIntent = getGoogleRequestIntent(requireActivity())
        launcher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        viewModel.loginWithGoogle(idToken)
    }

    private fun signOut() {
        loginManager.logOut()
        Log.d(TAG, "signOut: ")
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun loginWithFacebook() {
        if (isLoggedIn()) signOut()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
          //      Log.d(TAG, "onSuccess: $token")
                firebaseAuthWithFacebook(token)
            }

            override fun onCancel() {
                // Handle login cancel
            }

            override fun onError(error: FacebookException) {
                // Handle login error
                val msg = error.message ?: getString(R.string.generic_err_msg)
            //    Log.d(TAG, "onError: $msg")
                view?.showSnakeBarError(msg)
                logAuthIssueToCrashlytics(msg, "Facebook")
            }
        })

        loginManager.logInWithReadPermissions(
            this, callbackManager, listOf("email", "public_profile")
        )
    }

    private fun firebaseAuthWithFacebook(token: String) {
        viewModel.loginWithFacebook(token)
    }

    companion object {

        private const val TAG = "LoginFragment"
    }
}