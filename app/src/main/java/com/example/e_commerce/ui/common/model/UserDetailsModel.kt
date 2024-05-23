package com.example.e_commerce.ui.common.model

data class UserDetailsModel(
    val id: String,
    val email: String,
    val name: String,
    val reviews: List<String>,
)