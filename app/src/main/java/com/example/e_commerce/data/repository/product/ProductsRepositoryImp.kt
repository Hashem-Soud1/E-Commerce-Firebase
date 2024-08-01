package com.example.e_commerce.data.repository.product

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.home.ProductModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
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

    override suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot?
    ) = flow<Resource<QuerySnapshot>> {
        try {
            emit(Resource.Loading())

            var firstQuery = firestore.collection("product")
                .orderBy("price")

            if (lastDocument != null) {
                firstQuery = firstQuery.startAfter(lastDocument)
            }

            firstQuery = firstQuery.limit(pageLimit)

            val products = firstQuery.get().await()
            emit(Resource.Success(products))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }


}