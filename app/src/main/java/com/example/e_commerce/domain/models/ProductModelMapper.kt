package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.home.ProductModel
import com.example.e_commerce.ui.home.model.ProductUIModel


    fun ProductModel.toProductUIModel()=ProductUIModel(
            id = id ?: "",
            name = name ?: "",
            description =description ?: "",
            categoriesIDs = categoryID ?: emptyList(),
            images = imag ?: emptyList(),
            price = price ?: 0,
            rate =rate ?: 0.0f,
            priceAfterSale = 0,
            salePercentage = salePercentage,
            saleType = saleType,
            colors = colors ?: emptyList(),
            currencySymbol = ""
     )



