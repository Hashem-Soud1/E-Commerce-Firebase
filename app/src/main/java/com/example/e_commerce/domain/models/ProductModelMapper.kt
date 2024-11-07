package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.product.ProductColorModel
import com.example.e_commerce.data.models.product.ProductModel
import com.example.e_commerce.data.models.product.ProductSizeModel
import com.example.e_commerce.ui.home.model.ProductUIModel
import com.example.e_commerce.ui.product.model.ProductColorUIModel


fun ProductUIModel.toProductModel(): ProductModel {
        return ProductModel(
                id = id,
                name = name,
                description = description,
                categoriesIDs = categoriesIDs,
                imag = images,
                price = price,
                rate = rate,
                salePercentage = salePercentage,
                saleType = saleType,
                colors = colors.map {
                        ProductColorModel(
                                size = it.size,
                                stock = it.stock,
                                color = it.color
                        )
                },
                sizes = sizes.map {
                        ProductSizeModel(
                                size = it.size,
                                stock = it.stock
                        )
                }
        )
}

fun ProductModel.toProductUIModel(): ProductUIModel {
        return ProductUIModel(
                id = id ?: throw IllegalArgumentException("Product ID is missing"),
                name = name ?: "No Name",
                description = description ?: "No Description",
                categoriesIDs = categoriesIDs ?: emptyList(),
                images = imag ?: emptyList(),
                price = price ?: 0,
                rate = rate ?: 0f,
                salePercentage = salePercentage,
                saleType = saleType,
                colors = colors?.map {
                        ProductColorUIModel(
                                size = it.size,
                                stock = it.stock,
                                color = it.color
                        )
                } ?: emptyList(),
                sizes = sizes?.map {
                        ProductSizeModel(
                                size = it.size,
                                stock = it.stock
                        )
                } ?: emptyList()
        )
}

