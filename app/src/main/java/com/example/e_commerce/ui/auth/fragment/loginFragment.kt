package com.example.e_commerce.ui.auth.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.e_commerce.R
import com.example.e_commerce.data.datasource.datastore.UserPreferencesDataSource
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl

import com.example.e_commerce.data.repository.user.UserDataStoreRepositoryImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModel
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModelFactory
import java.util.zip.Inflater

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userPrefs = UserDataStoreRepositoryImpl(
                UserPreferencesDataSource(
                    requireActivity()
                )), authFirebase = FirebaseAuthRepositoryImpl()
        )
    }

    private var _binding: FragmentLoginBinding? = null
    private  val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewmodel=loginViewModel
        binding.lifecycleOwner=viewLifecycleOwner
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {

        private const val TAG = "LoginFragment"
    }
}