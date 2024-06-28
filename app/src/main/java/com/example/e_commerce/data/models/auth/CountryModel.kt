package com.example.e_commerce.data.models.auth

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CountryModel(
    val id: String? = null,
    val name: String? = null,

    @get:PropertyName("currnecy_symbool")
    val currnecySymbool: String? = null,

    val imag: String? = null,
    val currnecy: String? = null,


): Parcelable
