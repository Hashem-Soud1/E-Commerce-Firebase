package com.example.e_commerce.data.models.user

data class UserDetailsModel(
    val id: String,
    val email: String,
    val name: String,
    val reviews: List<String>,
)