package com.example.e_commerce.di

import com.example.e_commerce.data.data_source.networking.CloudFunctionAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {

        @Provides
        @Singleton
        fun provideCloudFunctionAPI(): CloudFunctionAPI {
            return CloudFunctionAPI.create()
        }
}