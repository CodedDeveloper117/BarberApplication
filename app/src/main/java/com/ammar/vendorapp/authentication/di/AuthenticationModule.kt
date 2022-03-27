package com.ammar.vendorapp.authentication.di

import com.ammar.vendorapp.authentication.data.api.UserAuthenticationApi
import com.ammar.vendorapp.authentication.data.repositories.AuthRepositoryImpl
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.*

@Module
@InstallIn(ViewModelComponent::class)
object AuthenticationModule {

    @Provides
    @ViewModelScoped
    fun provideUserAuthenticationApi(client: HttpClient) = UserAuthenticationApi(client)

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(api: UserAuthenticationApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }
}