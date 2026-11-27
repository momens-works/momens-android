package com.momens.android.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.core.common.extension.collectSideEffect
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.main.component.MomensMainTabBar
import com.momens.android.presentation.main.type.MainTab
import com.momens.android.presentation.onboarding.component.CoachmarkOverlay
import com.momens.android.presentation.onboarding.component.OnboardingSignalContent
import com.momens.android.presentation.onboarding.extension.offsetBy
import kotlinx.collections.immutable.persistentListOf

@Composable
fun OnboardingRoute(
    paddingValues: PaddingValues,
    navigateToSignal: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val step by viewModel.step.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            OnboardingSideEffect.NavigateToSignal -> navigateToSignal()
        }
    }

    OnboardingScreen(
        step = step,
        paddingValues = paddingValues,
        onNextClick = viewModel::onNextClick,
    )
}

@Composable
fun OnboardingScreen(
    step: OnboardingCoachmarkStep,
    paddingValues: PaddingValues,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var signalTitleBounds by remember { mutableStateOf<Rect?>(null) }
    var signalCardBounds by remember { mutableStateOf<Rect?>(null) }
    var minsuSuggestionBounds by remember { mutableStateOf<Rect?>(null) }
    var screenBounds by remember { mutableStateOf<Rect?>(null) }
    val targetBounds = when (step) {
        OnboardingCoachmarkStep.SIGNAL_TITLE -> signalTitleBounds
        OnboardingCoachmarkStep.SIGNAL_CARD -> signalCardBounds
        OnboardingCoachmarkStep.MINSU_SUGGESTION -> minsuSuggestionBounds
    }
    val overlayTargetBounds = targetBounds?.let { bounds ->
        screenBounds?.let { screenBounds ->
            bounds.offsetBy(
                horizontal = -screenBounds.left,
                vertical = -screenBounds.top,
            )
        } ?: bounds
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MomensTheme.colors.uiBg)
            .padding(paddingValues)
            .onGloballyPositioned { coordinates ->
                screenBounds = coordinates.boundsInRoot()
            },
    ) {
        OnboardingSignalContent(
            onSignalTitlePositioned = { signalTitleBounds = it },
            onSignalCardPositioned = { signalCardBounds = it },
            onMinsuSuggestionPositioned = { minsuSuggestionBounds = it },
        )

        MomensMainTabBar(
            tabs = persistentListOf(
                MainTab.SIGNAL,
                MainTab.BRIEF,
                MainTab.TASK,
            ),
            selectedTab = MainTab.SIGNAL,
            onTabClick = {},
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
        )

        CoachmarkOverlay(
            step = step,
            targetBounds = overlayTargetBounds,
            onNextClick = onNextClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    var step by remember { mutableStateOf(OnboardingCoachmarkStep.SIGNAL_TITLE) }

    MomensTheme {
        OnboardingScreen(
            step = step,
            paddingValues = PaddingValues(),
            onNextClick = {
                step = when (step) {
                    OnboardingCoachmarkStep.SIGNAL_TITLE -> OnboardingCoachmarkStep.SIGNAL_CARD
                    OnboardingCoachmarkStep.SIGNAL_CARD -> OnboardingCoachmarkStep.MINSU_SUGGESTION
                    OnboardingCoachmarkStep.MINSU_SUGGESTION -> OnboardingCoachmarkStep.SIGNAL_TITLE
                }
            },
        )
    }
}
