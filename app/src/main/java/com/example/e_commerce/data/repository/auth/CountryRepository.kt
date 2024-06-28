package com.example.e_commerce.data.repository.auth

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(): Flow<List<CountryModel>>
}