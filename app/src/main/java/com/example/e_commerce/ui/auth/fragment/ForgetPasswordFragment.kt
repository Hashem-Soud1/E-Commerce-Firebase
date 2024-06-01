package com.example.e_commerce.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentForgetPasswordBinding
import com.example.e_commerce.ui.auth.viewmodel.ForgetPasswordViewModel
import com.example.e_commerce.ui.auth.viewmodel.ForgetPasswordViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ForgetPasswordFragment : BottomSheetDialogFragment() {

    private val viewModel: ForgetPasswordViewModel by viewModels {
        ForgetPasswordViewModelFactory()
    }

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val TAG = "ForgetPasswordFragment"
    }
}