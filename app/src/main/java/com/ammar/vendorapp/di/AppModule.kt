package com.ammar.vendorapp.di

import com.ammar.vendorapp.data.firebase.Firestore
import com.ammar.vendorapp.data.repositories.AuthRepositoryImpl
import com.ammar.vendorapp.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): Firestore {
        return Firestore()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firestore: Firestore): AuthRepository {
        return AuthRepositoryImpl(firestore)
    }
}