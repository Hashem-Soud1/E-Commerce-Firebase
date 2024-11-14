package com.example.e_commerce.data.models.product

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
    var categoriesIDs: List<String>? = null,


    @get:PropertyName("sale_percentage")
    @set:PropertyName("sale_percentage")
    var salePercentage: Int? = null,

    @get:PropertyName("sale_type")
    @set:PropertyName("sale_type")
    var saleType: String? = null,
    var  rate : Float = 0.0f,
    var colors: List<ProductColorModel>? = null,
    var sizes: List<ProductSizeModel>? = null,
) : Parcelable

@Keep
@Parcelize
data class ProductColorModel(
    var size: String? = null,
    var stock: Int? = null,
    var color: String? = null
) : Parcelable

@Keep
@Parcelize
data class ProductSizeModel(
    var size: String? = null,
    var stock: Int? = null
) : Parcelable

enum class ProductSaleType(val type: String) {
    FLASH_SALE("flash_sale"),
    MEGA_SALE("mega_sale")
}