package com.momens.android.core.sdk.google

interface GoogleSignInLauncher {
    suspend fun launch(): Result<String>
}
