package com.momens.android.core.sdk.google.di

import com.momens.android.core.sdk.google.CredentialManagerGoogleSignInLauncher
import com.momens.android.core.sdk.google.GoogleSignInLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class GoogleSignInModule {

    @Binds
    @ActivityScoped
    abstract fun bindGoogleSignInLauncher(
        credentialManagerGoogleSignInLauncher: CredentialManagerGoogleSignInLauncher,
    ): GoogleSignInLauncher
}
