package com.example.e_commerce.data.models

import androidx.annotation.Keep
import java.io.Serializable
@Keep
class GenericResponse<T>(private val msg: String? = null) : Serializable {
    var code: Int? = null
    var data: T? = null
    var message: String? = msg
}