package com.example.e_commerce.ui.auth.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.e_commerce.R
import com.example.e_commerce.data.datasource.datastore.UserPreferencesDataSource

import com.example.e_commerce.data.repository.user.UserDataStoreRepositoryImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModel
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModelFactory
import java.util.zip.Inflater

class LoginFragment : Fragment() {

    val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userPrefs = UserDataStoreRepositoryImpl(
                UserPreferencesDataSource(
                    requireActivity()
                ))
        )
    }

    private var _binding: FragmentLoginBinding? = null
    private  val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentLoginBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {

        private const val TAG = "LoginFragment"
    }
}