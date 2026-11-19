package com.momens.android.presentation.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momens.android.core.local.onboarding.OnboardingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val step = savedStateHandle.getStateFlow(STEP_KEY, OnboardingCoachmarkStep.SIGNAL_TITLE)

    private val _sideEffect = MutableSharedFlow<OnboardingSideEffect>()
    val sideEffect: SharedFlow<OnboardingSideEffect> = _sideEffect.asSharedFlow()

    fun onNextClick() {
        when (step.value) {
            OnboardingCoachmarkStep.SIGNAL_TITLE -> savedStateHandle[STEP_KEY] = OnboardingCoachmarkStep.SIGNAL_CARD
            OnboardingCoachmarkStep.SIGNAL_CARD -> savedStateHandle[STEP_KEY] = OnboardingCoachmarkStep.MINSU_SUGGESTION
            OnboardingCoachmarkStep.MINSU_SUGGESTION -> completeOnboarding()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingManager.saveHasSeenOnboarding(hasSeen = true)
            _sideEffect.emit(OnboardingSideEffect.NavigateToSignal)
        }
    }

    private companion object {
        const val STEP_KEY = "onboarding_step"
    }
}
