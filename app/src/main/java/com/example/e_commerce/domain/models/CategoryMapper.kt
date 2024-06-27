package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.home.CategoryModel
import com.example.e_commerce.ui.home.model.CategoryUIModel


fun CategoryModel.toCategoryUIModel(): CategoryUIModel {
    return CategoryUIModel(
        id = id,
        name = name,
        icon = icon
    )
}