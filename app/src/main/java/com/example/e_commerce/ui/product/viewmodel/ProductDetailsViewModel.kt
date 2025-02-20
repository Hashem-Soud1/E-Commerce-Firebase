package com.example.e_commerce.ui.product.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.product.ProductsRepository
import com.example.e_commerce.domain.models.toProductUIModel
import com.example.e_commerce.ui.home.model.ProductUIModel
import com.example.e_commerce.ui.product.ProductDetailsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val productUiModel: ProductUIModel by lazy {
        savedStateHandle.get<ProductUIModel>(ProductDetailsActivity.PRODUCT_UI_MODEL_EXTRA)
            ?: throw IllegalArgumentException("ProductUIModel is required")
    }

    private val _productDetailsState: MutableStateFlow<ProductUIModel> =
        MutableStateFlow(productUiModel)
    val productDetailsState = _productDetailsState
        .asStateFlow()



    init {
        listenToProductDetails()
    }

    private fun listenToProductDetails() = viewModelScope.launch(IO) {
        productsRepository.listenToProductDetails(productUiModel.id).collectLatest {
            _productDetailsState.value = it.toProductUIModel()
        }
    }

    companion object {
        private const val TAG = "ProductDetailsViewModel"
    }
}