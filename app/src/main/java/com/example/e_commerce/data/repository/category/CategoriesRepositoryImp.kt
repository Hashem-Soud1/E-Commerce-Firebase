package com.example.e_commerce.data.repository.category

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.home.CategoryModel
import com.example.e_commerce.domain.models.toCategoryUIModel
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoriesRepositoryImp @Inject constructor(
    private  val firestore: FirebaseFirestore
): CategoriesRepository{
    override fun getCategories() = flow {
        try {
            val categories = firestore.collection("category")
                .get().await().toObjects(CategoryModel::class.java)

            emit(Resource.Success(categories.map { it.toCategoryUIModel() }))

        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}