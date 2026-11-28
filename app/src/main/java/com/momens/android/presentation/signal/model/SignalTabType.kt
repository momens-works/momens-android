package com.momens.android.presentation.signal.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.momens.android.core.designsystem.theme.MomensTheme

enum class SignalTabType(
    val contentColor: @Composable () -> Color,
    val lineColor: @Composable () -> Color,
    val textStyle: @Composable () -> TextStyle,
    val horizontalPadding: Dp,
) {
    ACTIVE(
        contentColor = { MomensTheme.colors.primary100 },
        lineColor = { MomensTheme.colors.primary100 },
        textStyle = { MomensTheme.typography.bodyB14 },
        horizontalPadding = 10.dp,
    ),

    DEFAULT(
        contentColor = { MomensTheme.colors.gray300 },
        lineColor = { Color.Transparent },
        textStyle = { MomensTheme.typography.bodyM14 },
        horizontalPadding = 16.dp,
    )
}
