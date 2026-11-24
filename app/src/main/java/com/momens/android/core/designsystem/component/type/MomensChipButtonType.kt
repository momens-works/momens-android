package com.momens.android.core.designsystem.component.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.momens.android.core.designsystem.theme.MomensTheme

enum class MomensChipButtonType(
    val textColor: @Composable () -> Color,
    val background: @Composable () -> Color,
    val textStyle: @Composable () -> TextStyle = { MomensTheme.typography.captionM10 },
) {
    CLICK(
        textColor = { MomensTheme.colors.white },
        background = { MomensTheme.colors.primary100 },
        textStyle = { MomensTheme.typography.bodyB12 },
    ),
    DEFAULT(
        textColor = { MomensTheme.colors.gray400 },
        background = { MomensTheme.colors.white },
        textStyle = { MomensTheme.typography.bodyB12 },
    ),
    PURPLE(
        textColor = { MomensTheme.colors.white },
        background = { MomensTheme.colors.pointPurple },
    ),
    RED(
        textColor = { MomensTheme.colors.white },
        background = { MomensTheme.colors.pointRed },
    ),
    BLACK(
        textColor = { MomensTheme.colors.white },
        background = { MomensTheme.colors.gray800 },
    ),
    WHITE(
        textColor = { MomensTheme.colors.gray800 },
        background = { MomensTheme.colors.white },
    ),
    YELLOW(
        textColor = { MomensTheme.colors.white },
        background = { MomensTheme.colors.pointYellow },
    ),
    MINT(
        textColor = { MomensTheme.colors.white },
        background = { MomensTheme.colors.pointMint },
    ),
}
