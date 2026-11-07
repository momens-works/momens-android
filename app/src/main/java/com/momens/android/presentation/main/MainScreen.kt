package com.momens.android.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.core.common.permission.RequestNotificationPermissionEffect
import com.momens.android.core.designsystem.component.snackbar.MomensSnackbar
import com.momens.android.core.designsystem.effect.momensNavBlurSource
import com.momens.android.core.designsystem.effect.rememberMomensNavBlurState
import com.momens.android.core.designsystem.trigger.GlobalUiEventHolder
import com.momens.android.core.designsystem.trigger.LocalGlobalUiEventTrigger
import com.momens.android.core.designsystem.trigger.rememberGlobalSnackbarController
import com.momens.android.core.model.fcm.PushData
import com.momens.android.core.model.fcm.PushDestination
import com.momens.android.presentation.main.component.MomensMainTabBar
import com.momens.android.presentation.main.navigation.MainAppState
import com.momens.android.presentation.main.navigation.MomensNavHost
import com.momens.android.presentation.main.navigation.rememberMainAppState
import com.momens.android.presentation.main.type.MainTab
import kotlinx.collections.immutable.toImmutableList

private val SNACKBAR_TAB_BAR_SPACING = 8.dp

@Composable
fun MainScreen(
    appState: MainAppState = rememberMainAppState(),
    pendingPushData: PushData? = null,
    onPushDataConsumed: () -> Unit = {},
) {
    val currentTab by appState.currentTab.collectAsStateWithLifecycle()
    val tabs = remember { MainTab.entries.toImmutableList() }
    val navBlurState = rememberMomensNavBlurState()
    val density = LocalDensity.current

    var tabBarBoxHeight by remember { mutableStateOf(0.dp) }
    var pendingSignalId by remember { mutableStateOf<String?>(null) }

    val snackbarController = rememberGlobalSnackbarController()
    val eventHolder = remember(snackbarController) {
        GlobalUiEventHolder(showSnackbar = snackbarController::show)
    }

    // 스플래시/로그인/온보딩에서는 currentTab이 null이므로, 로그인 후 메인 탭 화면에 진입했을 때 요청한다.
    RequestNotificationPermissionEffect(enabled = currentTab != null)

    LaunchedEffect(pendingPushData, currentTab) {
        val pushData = pendingPushData ?: return@LaunchedEffect
        if (currentTab == null) return@LaunchedEffect

        appState.navigateFromPushData(pushData)

        if (pushData.destination == PushDestination.SIGNAL_DETAIL) {
            pendingSignalId = pushData.signalId
        }

        onPushDataConsumed()
    }

    CompositionLocalProvider(
        LocalGlobalUiEventTrigger provides eventHolder,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .momensNavBlurSource(state = navBlurState),
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = {
                    val snackbarBottomPadding: Dp = if (currentTab != null) {
                        tabBarBoxHeight + SNACKBAR_TAB_BAR_SPACING
                    } else {
                        SNACKBAR_TAB_BAR_SPACING
                    }

                    SnackbarHost(hostState = snackbarController.snackbarHostState) {
                        val state = snackbarController.currentState ?: return@SnackbarHost

                        MomensSnackbar(
                            content = state.content,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = snackbarBottomPadding),
                        )
                    }
                },
            ) { innerPadding ->
                MomensNavHost(
                    appState = appState,
                    paddingValues = innerPadding,
                    tabBarHeight = tabBarBoxHeight,
                    pendingSignalId = pendingSignalId,
                    onPendingSignalConsumed = { pendingSignalId = null },
                )
            }

            currentTab?.let { tab ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .onGloballyPositioned { coordinates ->
                            tabBarBoxHeight = with(density) {
                                coordinates.size.height.toDp()
                            }
                        }
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    MomensMainTabBar(
                        tabs = tabs,
                        selectedTab = tab,
                        onTabClick = appState::navigate,
                        navBlurState = navBlurState,
                    )
                }
            }
        }
    }
}
