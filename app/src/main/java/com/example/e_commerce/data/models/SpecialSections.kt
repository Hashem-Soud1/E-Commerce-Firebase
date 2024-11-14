package com.example.e_commerce.data.models

enum class SpecialSections(val id: String) {
    RECOMMENDED_PRODUCTS(id = "XKZebKcKWAXZolSYm5zz"),
}


data class SpecialSectionModel(
    val id: String? = null,
    val title: String?= null,
    val description: String?= null,
    val imag: String?= null,
) {
    override fun toString(): String {
        return "SpecialSectionModel(id=$id, title=$title, description=$description,  image=$imag"
    }
}