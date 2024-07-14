package com.example.e_commerce.data.repository.product

import com.example.e_commerce.data.models.home.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImp @Inject constructor(
    val firestore: FirebaseFirestore
  ): ProductsRepository{
    override fun getSaleProducts( saleType: String, pageLimit: Int ) = flow {
        val products = firestore.collection("product")
            .whereEqualTo("sale_type", saleType)
            .orderBy("price")
            .get().await().toObjects(ProductModel::class.java)
        
        val repeatedProducts = mutableListOf<ProductModel>()
        for (i in 0 until pageLimit) {
            repeatedProducts.addAll(products)
        }

        emit(products)

    }


}