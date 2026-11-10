package com.momens.android.presentation.signin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momens.android.core.local.onboarding.OnboardingManager
import com.momens.android.core.sdk.google.GoogleSignInCancelledException
import com.momens.android.core.util.suspendRunCatching
import com.momens.android.data.signin.repository.SignInRepository
import com.momens.android.presentation.signin.SignInFeedback
import com.momens.android.presentation.signin.SignInSideEffect
import com.momens.android.presentation.signin.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val onboardingManager: OnboardingManager,
    private val networkChecker: SignInNetworkChecker,
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInUiState())
    val signInState: StateFlow<SignInUiState> = _signInState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SignInSideEffect>()
    val sideEffect: SharedFlow<SignInSideEffect> = _sideEffect.asSharedFlow()

    fun onGoogleSignInClick() {
        if (_signInState.value.isLoading) return

        if (!networkChecker.hasInternetConnection()) {
            _signInState.value = SignInUiState(feedback = SignInFeedback.NETWORK_FAILED)
            return
        }

        _signInState.value = SignInUiState(isLoading = true)

        viewModelScope.launch {
            _sideEffect.emit(SignInSideEffect.LaunchGoogleSignIn)
        }
    }

    fun onGoogleSignInResult(result: Result<String>) {
        result.fold(
            onSuccess = ::signInWithGoogle,
            onFailure = { throwable ->
                if (throwable is CancellationException) {
                    onGoogleSignInInterrupted()
                    throw throwable
                }
                _signInState.value = SignInUiState(
                    feedback = when {
                        !networkChecker.hasInternetConnection() -> SignInFeedback.NETWORK_FAILED
                        throwable is GoogleSignInCancelledException -> SignInFeedback.CANCELLED
                        throwable is IOException -> SignInFeedback.NETWORK_FAILED
                        else -> SignInFeedback.AUTHENTICATION_FAILED
                    },
                )
            },
        )
    }

    fun onGoogleSignInInterrupted() {
        _signInState.value = SignInUiState()
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            suspendRunCatching {
                signInRepository.signInWithGoogle(idToken).getOrThrow()
                onboardingManager.getHasSeenOnboarding()
            }
                .onSuccess { hasSeenOnboarding ->
                    val sideEffect = if (hasSeenOnboarding) {
                        SignInSideEffect.NavigateToSignal
                    } else {
                        SignInSideEffect.NavigateToOnboarding
                    }
                    _sideEffect.emit(sideEffect)
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "Google 로그인 또는 bootstrap 처리 실패")
                    _signInState.value = SignInUiState(
                        feedback = when {
                            throwable is IOException -> SignInFeedback.NETWORK_FAILED
                            throwable is HttpException && throwable.code() in listOf(401, 403) ->
                                SignInFeedback.AUTHENTICATION_FAILED

                            else -> SignInFeedback.SERVICE_FAILED
                        },
                    )
                }
        }
    }
}
