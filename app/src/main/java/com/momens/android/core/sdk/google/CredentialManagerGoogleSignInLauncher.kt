package com.momens.android.core.sdk.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import javax.inject.Named

class CredentialManagerGoogleSignInLauncher @Inject constructor(
    @ActivityContext private val activityContext: Context,
    private val credentialManager: CredentialManager,
    @Named("google_server_client_id")
    private val googleWebClientId: String,
) : GoogleSignInLauncher {

    override suspend fun launch(): Result<String> = runCatching {
        require(googleWebClientId.isNotBlank()) {
            "google_server_client_id is blank."
        }

        val googleIdOption = GetSignInWithGoogleOption.Builder(
            googleWebClientId,
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credential = credentialManager.getCredential(
            context = activityContext,
            request = request,
        ).credential

        if (
            credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            throw GoogleSignInFailedException(
                message = "Invalid Google credential type.",
            )
        }

        GoogleIdTokenCredential.createFrom(credential.data).idToken
    }.recoverCatching { throwable ->
        when (throwable) {
            is GetCredentialCancellationException -> {
                throw GoogleSignInCancelledException()
            }

            is GetCredentialException -> {
                throw GoogleSignInFailedException(
                    message = throwable.message ?: "Google login failed.",
                    cause = throwable,
                )
            }

            else -> throw throwable
        }
    }
}
