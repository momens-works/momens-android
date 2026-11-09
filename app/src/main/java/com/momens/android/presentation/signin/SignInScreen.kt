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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.R
import com.momens.android.core.common.extension.collectSideEffect
import com.momens.android.core.common.state.UiState
import com.momens.android.core.designsystem.component.button.MomensCtaButton
import com.momens.android.core.designsystem.component.logo.MomensLogo
import com.momens.android.core.designsystem.component.type.MomensCtaType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.core.sdk.google.rememberGoogleSignInLauncher

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
                viewModel.onGoogleSignInResult(
                    googleSignInLauncher.launch(),
                )
            }

            SignInSideEffect.NavigateToOnboarding -> navigateToOnboarding()

            SignInSideEffect.NavigateToSignal -> navigateToSignal()
        }
    }

    SignInScreen(
        paddingValues = paddingValues,
        isGoogleLoginEnabled = signInState !is UiState.Loading,
        onGoogleLoginClick = viewModel::onGoogleSignInClick,
    )
}

@Composable
private fun SignInScreen(
    paddingValues: PaddingValues,
    isGoogleLoginEnabled: Boolean,
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

            MomensCtaButton(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = onGoogleLoginClick,
                type = MomensCtaType.LOGIN,
                enabled = isGoogleLoginEnabled,
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
            isGoogleLoginEnabled = true,
            onGoogleLoginClick = {},
        )
    }
}
