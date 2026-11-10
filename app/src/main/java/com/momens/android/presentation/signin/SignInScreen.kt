package com.momens.android.presentation.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.R
import com.momens.android.core.common.extension.collectSideEffect
import com.momens.android.core.designsystem.component.button.MomensCtaButton
import com.momens.android.core.designsystem.component.logo.MomensLogo
import com.momens.android.core.designsystem.component.type.MomensCtaType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.core.sdk.google.rememberGoogleSignInLauncher
import com.momens.android.presentation.signin.viewmodel.SignInViewModel
import kotlinx.coroutines.CancellationException

@Composable
fun SignInRoute(
    paddingValues: PaddingValues,
    navigateToOnboarding: () -> Unit,
    navigateToSignal: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()
    val googleSignInLauncher = rememberGoogleSignInLauncher()

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SignInSideEffect.LaunchGoogleSignIn -> {
                try {
                    viewModel.onGoogleSignInResult(googleSignInLauncher.launch())
                } catch (exception: CancellationException) {
                    viewModel.onGoogleSignInInterrupted()
                    throw exception
                }
            }

            SignInSideEffect.NavigateToOnboarding -> navigateToOnboarding()

            SignInSideEffect.NavigateToSignal -> navigateToSignal()
        }
    }

    SignInScreen(
        paddingValues = paddingValues,
        uiState = signInState,
        onGoogleLoginClick = viewModel::onGoogleSignInClick,
    )
}

@Composable
private fun SignInScreen(
    paddingValues: PaddingValues,
    uiState: SignInUiState,
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MomensTheme.colors.primary100)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(145f))

        MomensLogo(
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            modifier = Modifier.weight(382f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(273f))

            uiState.feedback?.let { feedback ->
                Text(
                    text = when (feedback) {
                        SignInFeedback.CANCELLED -> "로그인을 취소했어요. 계속하려면 Google 로그인 버튼을 눌러주세요."
                        SignInFeedback.AUTHENTICATION_FAILED -> "Google 인증에 실패했어요. 계정을 확인하고 다시 시도해 주세요."
                        SignInFeedback.NETWORK_FAILED -> "연결에 실패했어요. 네트워크 상태를 확인하고 다시 시도해 주세요."
                        SignInFeedback.SERVICE_FAILED -> "로그인을 완료하지 못했어요. 잠시 후 다시 시도해 주세요."
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    color = MomensTheme.colors.white,
                    style = MomensTheme.typography.bodyB16,
                    textAlign = TextAlign.Center,
                )
            }

            MomensCtaButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .semantics { role = Role.Button },
                onClick = onGoogleLoginClick,
                type = MomensCtaType.LOGIN,
                enabled = !uiState.isLoading,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )

                    Text(
                        text = "Continue with Google",
                        color = MomensTheme.colors.gray700,
                        style = MomensTheme.typography.bodyB16,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(15f))

            Text(
                text = "계속하면 서비스 약관과 개인정보 처리방침에 동의하게 됩니다.",
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MomensTheme.colors.gray200,
                style = MomensTheme.typography.captionM10,
            )

            Spacer(modifier = Modifier.weight(22f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    MomensTheme {
        SignInScreen(
            paddingValues = PaddingValues(),
            uiState = SignInUiState(),
            onGoogleLoginClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenFailurePreview() {
    MomensTheme {
        SignInScreen(
            paddingValues = PaddingValues(),
            uiState = SignInUiState(feedback = SignInFeedback.NETWORK_FAILED),
            onGoogleLoginClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenCancelledPreview() {
    MomensTheme {
        SignInScreen(
            paddingValues = PaddingValues(),
            uiState = SignInUiState(feedback = SignInFeedback.CANCELLED),
            onGoogleLoginClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenLoadingPreview() {
    MomensTheme {
        SignInScreen(
            paddingValues = PaddingValues(),
            uiState = SignInUiState(isLoading = true),
            onGoogleLoginClick = {},
        )
    }
}
