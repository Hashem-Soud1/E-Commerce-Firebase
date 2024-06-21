package com.example.e_commerce.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdsRepository: SalesAdsRepository
):ViewModel() {

val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
    viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading())


}