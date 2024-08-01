package com.example.e_commerce.data.repository.product

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.home.ProductModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getSaleProducts(countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>>


    suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot? = null
    ): Flow<Resource<QuerySnapshot>>

}