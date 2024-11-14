package com.example.e_commerce.di

import com.example.e_commerce.data.repository.auth.CountryRepository
import com.example.e_commerce.data.repository.auth.CountryRepositoryImpl
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepository
import com.example.e_commerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.category.CategoriesRepository
import com.example.e_commerce.data.repository.category.CategoriesRepositoryImp
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepositoryImp
import com.example.e_commerce.data.repository.product.ProductsRepository
import com.example.e_commerce.data.repository.product.ProductsRepositoryImp
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepositoryImp
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepositoryImpl
import com.training.ecommerce.data.repository.special_sections.SpecialSectionsRepository
import com.training.ecommerce.data.repository.special_sections.SpecialSectionsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFirebaseAuthRepository(
        firebaseAuthRepositoryImpl: FirebaseAuthRepositoryImpl
    ): FirebaseAuthRepository


    @Binds
    @Singleton
    abstract fun bindUserPreferenceRepository(
        userPreferenceRepositoryImpl: UserPreferenceRepositoryImpl
    ): UserPreferenceRepository

    @Binds
    @Singleton
    abstract fun bindAppPreferenceRepository(
        appPreferenceRepositoryImpl: AppDataStoreRepositoryImpl
    ): AppPreferenceRepository

    @Binds
    @Singleton
    abstract fun bindUserFirestoreRepository(
            userFirestoreRepositoryImpl: UserFirestoreRepositoryImp
    ): UserFirestoreRepository

    @Binds
    @Singleton
    abstract fun bindSalesAdsRepository(
        salesAdsRepositoryImp: SalesAdsRepositoryImp
    ): SalesAdsRepository

    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(
        categoriesRepositoryImp: CategoriesRepositoryImp
    ): CategoriesRepository

    @Binds
    @Singleton
    abstract fun bindCountryRepository(
        countryRepositoryImp: CountryRepositoryImpl
    ): CountryRepository

    @Binds
    @Singleton
    abstract fun bindProductsRepository(
        productsRepositoryImp: ProductsRepositoryImp
    ): ProductsRepository

    @Binds
    @Singleton
    abstract fun provideSpecialSectionsRepositoryImpl(
        specialSectionsRepository: SpecialSectionsRepositoryImpl
    ): SpecialSectionsRepository


}