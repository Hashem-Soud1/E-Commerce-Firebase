package com.example.e_commerce.ui.home.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.e_commerce.data.models.product.ProductSizeModel
import com.example.e_commerce.ui.product.model.ProductColorUIModel
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ProductUIModel(
    val id: String,
    val name: String,
    val description: String,
    val colors: List<ProductColorUIModel>,
    val sizes: List<ProductSizeModel>,
    val categoriesIDs: List<String>,  // Assuming categories are always provided, but can be empty.
    val images: List<String>,        // Image URLs can also be an empty list if there are no images.
    val price: Int,                  // Presenting price as a non-nullable Int for simplicity in UI calculations and display.
    val rate: Float,                  // Presenting price as a non-nullable Int for simplicity in UI calculations and display.
    val priceAfterSale: Int? = null,      // Default price after sale is 0.
    val salePercentage: Int?,       // Offer percentage can be nullable to indicate no current offers.
    val saleType: String?,           // Sale type can be nullable if not all products are on sale.
    val currencySymbol: String = ""     // Default currency is USD.
) : Parcelable {


val formatPrice  get()= "$currencySymbol$price"

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
