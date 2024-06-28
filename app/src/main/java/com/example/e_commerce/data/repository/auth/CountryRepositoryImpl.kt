package com.example.e_commerce.data.repository.auth

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CountryRepository {
    override fun getCountries()= flow {

        try {
            val countries = firestore.collection("country")
                .get().await().toObjects(CountryModel::class.java)

            val repeatCountry = mutableListOf<CountryModel>()
                repeat(10) {
                    repeatCountry.addAll(countries)
                }

            emit(repeatCountry)


        }catch (e: Exception){
            Log.d("CountryRepositoryImpl", "getCountries: ${e.message}")

        }

    }

}