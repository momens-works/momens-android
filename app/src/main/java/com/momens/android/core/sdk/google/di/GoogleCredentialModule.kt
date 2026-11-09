package com.momens.android.core.sdk.google.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.momens.android.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleCredentialModule {

    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context,
    ): CredentialManager = CredentialManager.create(context)

    @Provides
    @Singleton
    @Named("google_server_client_id")
    fun provideGoogleServerClientId(
        @ApplicationContext context: Context,
    ): String = context.getString(R.string.google_server_client_id)
}
