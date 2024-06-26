package com.example.e_commerce.data.repository.home

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.home.SalesAdModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SalesAdsRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : SalesAdsRepository{
    override fun getSalesAds() = flow {
        try {
            val salesAds = firestore.collection("sales_ad")
                .get().await().toObjects(SalesAdModel::class.java)

            emit(Resource.Success(salesAds.map { it.toUIModel()}))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

}