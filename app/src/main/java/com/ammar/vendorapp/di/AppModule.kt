package com.ammar.vendorapp.di

import android.content.Context
import com.ammar.vendorapp.authentication.data.repositories.DatastoreRepository
import com.ammar.vendorapp.common.api.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient() = KtorClient.httpClient

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context): DatastoreRepository {
        return DatastoreRepository(context)
    }

}