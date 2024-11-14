package com.example.e_commerce.ui.auth.fragment

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.databinding.FragmentCountryBinding
import com.example.e_commerce.ui.auth.adapter.CountryAdapter
import com.example.e_commerce.ui.auth.adapter.CountryClickListener
import com.example.e_commerce.ui.auth.viewmodel.CountryViewModel
import com.example.e_commerce.ui.common.fragments.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class CountryFragment :BaseBottomSheetFragment<FragmentCountryBinding, CountryViewModel>(){

    override val viewModel: CountryViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_country
    override fun init() {
      initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.countryState.collect {
             initAdapter(it)

            }
        }
    }

    private fun initAdapter(data: List<CountryModel>?) {
val countryadapter = CountryAdapter(data!!, object : CountryClickListener {
            override fun onCountryClicked(country: CountryModel) {
                viewModel.saveUserCountry(country)
                dismiss()
            }
        })


    binding.countriesRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countryadapter
            setHasFixedSize(true)
                        }

 }





}