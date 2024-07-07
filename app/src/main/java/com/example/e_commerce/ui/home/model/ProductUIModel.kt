package com.example.e_commerce.ui.home.model

data class ProductUIModel(
    val id: String,
    val name: String,
    val description: String,
    val categoriesIDs: List<String>,
    val images: List<String>,
    val price: Int,
    val rate: Float,
    val priceAfterSale: Int? = null,
    val salePercentage: Int?,
    val saleType: String?,
    val colors: List<String>,
    val currencySymbol: String?
) {



    fun getFormattedPrice(): String {
        return "$currencySymbol$price"
    }

    fun getFormattedPriceAfterSale(): String {
        if (saleType == null || salePercentage == null) return getFormattedPrice()
        val newPrice = salePercentage.let { price.minus(price * it / 100) }
        return "$currencySymbol$newPrice"
    }

    fun getFormattedSale(): String {
        return "$salePercentage%"
    }

    fun getFirstImage(): String {
        return images.firstOrNull() ?: ""
    }




}
