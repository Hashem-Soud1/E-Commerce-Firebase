package com.example.e_commerce.data.repository.product

import com.example.e_commerce.data.models.home.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getSaleProducts( saleType: String, pageLimit: Int) : Flow<List<ProductModel>>
}