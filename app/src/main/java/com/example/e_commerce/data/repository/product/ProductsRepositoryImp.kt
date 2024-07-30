package com.example.e_commerce.data.repository.product

import android.util.Log
import com.example.e_commerce.data.models.home.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImp @Inject constructor(
    val firestore: FirebaseFirestore
  ): ProductsRepository{
    override fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>> {
        return flow {
            val products = firestore.collection("product")
                .whereEqualTo("sale_type", saleType)
                .whereEqualTo("country_id", countryID).
                orderBy("price").limit(pageLimit.toLong())
                .get().await().toObjects(ProductModel::class.java)

            emit(products)
        }
    }


}