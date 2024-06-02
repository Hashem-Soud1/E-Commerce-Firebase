package com.example.e_commerce.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentForgetPasswordBinding
import com.example.e_commerce.ui.auth.viewmodel.ForgetPasswordViewModel
import com.example.e_commerce.ui.auth.viewmodel.ForgetPasswordViewModelFactory
import com.example.e_commerce.ui.common.model.ProgressDialog
import com.example.e_commerce.utils.showSnakeBarError
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch


class ForgetPasswordFragment : BottomSheetDialogFragment() {

    private val viewModel: ForgetPasswordViewModel by viewModels {
        ForgetPasswordViewModelFactory()
    }

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.forgetPasswordState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        // Show loading
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        showSentEmailSuccessDialog()
                    }

                    is Resource.Error -> {
                        // Show error message
                        progressDialog.dismiss()
                        val msg = state.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                    }
                }
            }
        }
    }

    private fun showSentEmailSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Reset Password")
            .setMessage("We have sent you an email to reset your password. Please check your email.")
            .setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog?.dismiss()
                this@ForgetPasswordFragment.dismiss()
            }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val TAG = "ForgetPasswordFragment"
    }
}