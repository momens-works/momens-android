package com.momens.android.core.sdk.google.di

import com.momens.android.core.sdk.google.GoogleSignInLauncher
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface GoogleSignInEntryPoint {
    fun googleSignInLauncher(): GoogleSignInLauncher
}
