package com.example.e_commerce.data.models.home

import android.os.Parcelable
import androidx.annotation.Keep

import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,

    var imag: List<String>? = null,
    var price: Int? = null,

    @get:PropertyName("category_id")
    @set:PropertyName("category_id")
    var categoryID: List<String>? = null,


    @get:PropertyName("sale_percentage")
    @set:PropertyName("sale_percentage")
    var salePercentage: Int? = null,

    @get:PropertyName("sale_type")
    @set:PropertyName("sale_type")
    var saleType: String? = null,
    var colors: List<String>? = null,

    var  rate : Float = 0.0f
) : Parcelable

enum class ProductSaleType(val type: String) {
    FLASH_SALE("flash_sale"),
    MEGA_SALE("mega_sale")
}