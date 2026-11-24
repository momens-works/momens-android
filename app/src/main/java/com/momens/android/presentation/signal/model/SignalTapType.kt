package com.momens.android.presentation.signal.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.momens.android.core.designsystem.theme.MomensTheme

enum class SignalTapType(
    val title: String,
    val contentColor: @Composable () -> Color,
    val lineColor: @Composable () -> Color,
    val textStyle: @Composable () -> TextStyle,
    val horizontalPadding: Dp,
) {
    ACTIVE(
        title = "확인 필요",
        contentColor = { MomensTheme.colors.primary100 },
        lineColor = { MomensTheme.colors.primary100 },
        textStyle = { MomensTheme.typography.bodyB14 },
        horizontalPadding = 10.dp,
    ),
    DEFAULT(
        title = "검토 완료",
        contentColor = { MomensTheme.colors.gray300 },
        lineColor = { Color.Transparent },
        textStyle = { MomensTheme.typography.bodyM14 },
        horizontalPadding = 16.dp,
    )
}
