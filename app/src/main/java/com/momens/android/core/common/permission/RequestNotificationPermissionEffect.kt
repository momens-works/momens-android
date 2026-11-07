package com.momens.android.core.common.permission

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

/**
 * 알림 권한을 요청합니다.
 *
 * [enabled]가 true가 되는 시점(예: 로그인 후 메인 탭 화면 진입)에 한 번만 요청합니다.
 * 탭 화면과 상세 화면을 오가며 [enabled]가 바뀌어도 이미 요청했다면 다시 요청하지 않습니다.
 */
@Composable
fun RequestNotificationPermissionEffect(
    enabled: Boolean,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    var hasRequested by rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        Timber.tag(TAG).d("알림 권한 요청 결과: $isGranted")
    }

    LaunchedEffect(enabled) {
        if (!enabled || hasRequested) return@LaunchedEffect

        hasRequested = true
        if (!context.hasNotificationPermission()) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

private const val TAG = "NotificationPermission"
