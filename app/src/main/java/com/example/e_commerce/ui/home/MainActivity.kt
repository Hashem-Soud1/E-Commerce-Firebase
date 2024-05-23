
package com.example.e_commerce.ui.home



import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.data.datasource.datastore.AppPreferenceDataStore
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.ui.common.viewmodel.UserViewModel
import com.example.e_commerce.ui.common.viewmodel.UserViewModelFactory
import com.example.e_commerce.ui.auth.AuthActivity
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)

            val isLoggedIn = runBlocking { userViewModel.isUserLoggedIn().first()}

            if (!isLoggedIn)
            {
                goToAuthActivity()

          } else {
                setContentView(R.layout.activity_main)
                findViewById<TextView>(R.id.maintext).setOnClickListener {
                    logOut()
                }
            }
        initViewModel()
    }



    private fun initViewModel() {

        lifecycleScope.launch {
            val userDetails = runBlocking { userViewModel.getUserPrefsDetails().first() }
            Log.d(TAG, "initViewModel: user details ${userDetails.email}")

            userViewModel.userPrefsState.collect {
                Log.d(TAG, "initViewModel: user details updated ${it?.email}")
            }

        }
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptions.makeCustomAnimation(
            this, android.R.anim.fade_in, android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
    }

    private fun logOut() {
        lifecycleScope.launch {
            userViewModel.logout()
            goToAuthActivity()
        }
    }

    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()

        } else {
            setTheme(R.style.Theme_ECommerce)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}