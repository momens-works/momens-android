package com.momens.android.data.signin.di

import com.momens.android.data.signin.local.datasource.DeviceLocalDataSource
import com.momens.android.data.signin.local.datasource.GoogleCredentialLocalDataSource
import com.momens.android.data.signin.local.datasourceimpl.DeviceLocalDataSourceImpl
import com.momens.android.data.signin.local.datasourceimpl.GoogleCredentialLocalDataSourceImpl
import com.momens.android.data.signin.remote.datasource.SignInRemoteDataSource
import com.momens.android.data.signin.remote.datasourceimpl.SignInRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindGoogleCredentialLocalDataSource(
        googleCredentialLocalDataSourceImpl: GoogleCredentialLocalDataSourceImpl,
    ): GoogleCredentialLocalDataSource

    @Binds
    @Singleton
    abstract fun bindSignInRemoteDataSource(
        signInRemoteDataSourceImpl: SignInRemoteDataSourceImpl,
    ): SignInRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDeviceLocalDataSource(
        deviceLocalDataSourceImpl: DeviceLocalDataSourceImpl,
    ): DeviceLocalDataSource
}
