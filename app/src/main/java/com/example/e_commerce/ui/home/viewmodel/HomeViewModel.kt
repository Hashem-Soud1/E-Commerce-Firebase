package com.example.e_commerce.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.home.ProductModel
import com.example.e_commerce.data.models.home.ProductSaleType
import com.example.e_commerce.data.models.user.CountryDetails
import com.example.e_commerce.data.models.user.UserDetailsPreferences
import com.example.e_commerce.data.repository.category.CategoriesRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.data.repository.product.ProductsRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.domain.models.toProductUIModel
import com.example.e_commerce.ui.home.model.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdsRepository: SalesAdsRepository,
    categoriesRepository: CategoriesRepository,
    userPreferenceRepository: UserPreferenceRepository,
    val productsRepository: ProductsRepository
):ViewModel() {

val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
    viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading())

    val categoryState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading())

    private val countryState = userPreferenceRepository.getUserCountry().stateIn(
                viewModelScope + IO,
                started = SharingStarted.Eagerly,
                initialValue = CountryDetails.getDefaultInstance())




    val flashSalesState = productsRepository.getSaleProducts("flash_sale",5)

        .map { products ->
            products.map { it.toProductUIModel().copy(

                currencySymbol = countryState.value?.currencySymbool ?: ""
            )
            }

        }
        .stateIn(
            viewModelScope + IO,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )



//    val flashSalesState = getProductsSales(ProductSaleType.FLASH_SALE)
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun getProductsSales(productSaleType: ProductSaleType): StateFlow<List<ProductUIModel>> =
//        countryState.mapLatest {
//            productsRepository.getSaleProducts( productSaleType.type, 10)
//        }.mapLatest { it.first().map { getProductModel(it) } }.stateIn(
//            viewModelScope + IO,
//            started = SharingStarted.Eagerly,
//            initialValue = emptyList()
//        )
//
//
//    private fun getProductModel(product: ProductModel): ProductUIModel {
//        val productUIModel = product.toProductUIModel().copy(
//            currencySymbol = countryState.value?.currencySymbool ?: ""
//        )
//        return productUIModel
//    }


}