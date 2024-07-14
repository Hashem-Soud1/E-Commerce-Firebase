package com.training.ecommerce.data.repository.special_sections

import android.util.Log
import com.example.e_commerce.data.models.SpecialSectionModel
import com.example.e_commerce.data.models.SpecialSections
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.SpecialSectionsException
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpecialSectionsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SpecialSectionsRepository {
    override fun recommendProductsSection() = flow {
        try {
            val specialSection = firestore.collection("special_sections")
                .document(SpecialSections.RECOMMENDED_PRODUCTS.id).get().await()
                .toObject(SpecialSectionModel::class.java)

            emit(specialSection)
        } catch (e: Exception) {
            val msg = e.message ?: "Error fetching recommended products"
            CrashlyticsUtils.sendCustomLogToCrashlytics<SpecialSectionsException>(
                msg, Pair(CrashlyticsUtils.SPECIAL_SECTIONS, msg)
            )
            emit(null)
        }
    }

    companion object {
        private const val TAG = "SpecialSectionsRepositoryImpl"
    }
}