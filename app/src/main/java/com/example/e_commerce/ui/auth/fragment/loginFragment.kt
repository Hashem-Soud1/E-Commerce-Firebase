package com.example.e_commerce.ui.auth.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.data.datasource.datastore.UserPreferencesDataSource
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl

import com.example.e_commerce.data.repository.user.UserDataStoreRepositoryImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModel
import com.example.e_commerce.ui.auth.viewmodel.LoginViewModelFactory
import com.example.e_commerce.ui.common.repository.ProgressDialog
import kotlinx.coroutines.launch
import java.util.zip.Inflater

class LoginFragment : Fragment() {

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

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

     onClickLoginbtn()
     authLogin()
    }

    private fun onClickLoginbtn() {
    binding.loginBtn.setOnClickListener{
     loginViewModel.login()

    }
    }

    private fun authLogin() {

        lifecycleScope.launch{
          loginViewModel.loginState.collect{ resource ->
            when(resource)
            {
                is Resource.Loading ->  { progressDialog.show()

                }
                is Resource.Success ->  {progressDialog.dismiss()
                Toast.makeText(requireContext(),"Succed",Toast.LENGTH_LONG).show()
                }
                is Resource.Error   -> {progressDialog.dismiss()
                    Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                }
            }
        }
    }



}




    companion object {

        private const val TAG = "LoginFragment"
    }
}