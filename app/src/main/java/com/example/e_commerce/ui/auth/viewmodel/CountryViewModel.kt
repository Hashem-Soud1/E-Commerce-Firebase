package com.example.e_commerce.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.data.repository.auth.CountryRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CountryViewModel  @Inject constructor(
   private val countryRepository: CountryRepository,
   private val userPreferenceRepository: UserPreferenceRepository
) :ViewModel() {


    val countryState = countryRepository.getCountries().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )


    fun saveUserCountry(country: CountryModel) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserCountry(country)
        }

    }

}