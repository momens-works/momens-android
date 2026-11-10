package com.momens.android.presentation.signin

enum class SignInFeedback {
    CANCELLED,
    AUTHENTICATION_FAILED,
    NETWORK_FAILED,
    SERVICE_FAILED,
}

data class SignInUiState(
    val isLoading: Boolean = false,
    val feedback: SignInFeedback? = null,
)

sealed interface SignInSideEffect {
    data object LaunchGoogleSignIn : SignInSideEffect

    data object NavigateToOnboarding : SignInSideEffect

    data object NavigateToSignal : SignInSideEffect
}
