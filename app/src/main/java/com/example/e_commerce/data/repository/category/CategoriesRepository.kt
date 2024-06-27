package com.example.e_commerce.data.repository.category

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.ui.home.model.CategoryUIModel
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
     fun getCategories() : Flow<Resource<List<CategoryUIModel>>>
}