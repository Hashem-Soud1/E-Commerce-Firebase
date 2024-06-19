
package com.example.e_commerce.ui.home



import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ActivityMainBinding
import com.example.e_commerce.ui.common.viewmodel.UserViewModel
import com.example.e_commerce.ui.auth.AuthActivity
import com.example.e_commerce.ui.home.adapter.HomeViewPagerAdapter
import com.example.e_commerce.ui.home.fragments.HomeFragment
import com.training.ecommerce.ui.account.fragments.AccountFragment
import com.training.ecommerce.ui.cart.fragments.CartFragment
import com.training.ecommerce.ui.explore.fragments.ExploreFragment
import com.training.ecommerce.ui.offers.OffersFragment
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels ()

    private var _bindig: ActivityMainBinding? = null
    private val binding get() = _bindig!!
    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)

            val isLoggedIn = runBlocking { userViewModel.isUserLoggedIn().first()}

            if (!isLoggedIn)
                goToAuthActivity()
           else
               setContentView(R.layout.activity_main)


        _bindig = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initViewPger()
        initViewModel()
    }

    private fun initViewPger() {
        val fragments= listOf(
            HomeFragment(),
            ExploreFragment(),
            CartFragment(),
            OffersFragment(),
            AccountFragment()
        )

        val adapter = HomeViewPagerAdapter(this, fragments)
        binding.homeViewPager.adapter = adapter
        binding.homeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.homef
                    1 -> binding.bottomNavigationView.selectedItemId = R.id.exploref
                    2 -> binding.bottomNavigationView.selectedItemId = R.id.cartf
                    3 -> binding.bottomNavigationView.selectedItemId = R.id.offerf
                    4 -> binding.bottomNavigationView.selectedItemId = R.id.accountf
                }
               }
        })
    }

    private fun initViews() {
        initBottomNavigationView()
    }
    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homef -> binding.homeViewPager.currentItem = 0
                R.id.exploref -> binding.homeViewPager.currentItem = 1
                R.id.cartf -> binding.homeViewPager.currentItem = 2
                R.id.offerf -> binding.homeViewPager.currentItem = 3
                R.id.accountf -> binding.homeViewPager.currentItem = 4
            }
            true
        }
    }

    private fun initViewModel() {

        lifecycleScope.launch {
            val userDetails = runBlocking { userViewModel.getUserDetails().first() }

            userViewModel.userDetailsState.collect {
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



    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
             installSplashScreen()

         else
             setTheme(R.style.Theme_ECommerce)

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}