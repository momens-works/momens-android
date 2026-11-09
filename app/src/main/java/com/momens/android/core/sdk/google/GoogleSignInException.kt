package com.momens.android.core.sdk.google

class GoogleSignInCancelledException : Exception("Google login cancelled")

class GoogleSignInFailedException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause)
