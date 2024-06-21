package com.example.e_commerce.data.models.home

import android.os.Parcelable
import com.example.e_commerce.ui.common.model.SalesAdUIModel
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize
import java.util.Date

@Keep
@Parcelize
data class SalesAdModel(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    var imagUrl: String? = null,
    val type: String? = null,
    var productId: String? = null,
    var categoryId: String? = null,
    var externalLink: String? = null,
    var endAt: Date? = null
): Parcelable {
    fun toUIModel(): SalesAdUIModel {
        return SalesAdUIModel(
            id = this.id,
            title = this.title,
            description = this.description,
            imagUrl = this.imagUrl,
            type = this.type,
            productId = this.productId,
            categoryId = this.categoryId,
            externalLink = this.externalLink,
            endAt = this.endAt
        )
    }
}

