package com.momens.android.core.designsystem.component.type

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.momens.android.R
import com.momens.android.core.designsystem.theme.MomensTheme
import kotlinx.serialization.Serializable

@Serializable
enum class ImportantLevel(
    val label: String,
    val text: String,
    @param:DrawableRes val grayIcon: Int,
    @param:DrawableRes val blueIcon: Int,
) {
    LOW(
        label = "low",
        text = "낮음",
        grayIcon = R.drawable.ic_importance_low_gray,
        blueIcon = R.drawable.ic_importance_low_blue,
    ),
    MEDIUM(
        label = "medium",
        text = "중간",
        grayIcon = R.drawable.ic_importance_medium_gray,
        blueIcon = R.drawable.ic_importance_medium_blue,
    ),
    HIGH(
        label = "high",
        text = "높음",
        grayIcon = R.drawable.ic_importance_high_gray,
        blueIcon = R.drawable.ic_importance_high_blue,
    ),
    URGENT(
        label = "urgent",
        text = "긴급",
        grayIcon = R.drawable.ic_importance_high_gray,
        blueIcon = R.drawable.ic_importance_high_blue,
    ),
}

enum class ImportantTone(
    val textStyle: @Composable () -> TextStyle,
    val textColor: @Composable () -> Color,
    val background: @Composable () -> Color,
) {
    GRAY(
        textStyle = { MomensTheme.typography.bodyM12 },
        textColor = { MomensTheme.colors.gray700 },
        background = { MomensTheme.colors.gray100 },
    ),
    BLUE(
        textStyle = { MomensTheme.typography.bodyB12 },
        textColor = { MomensTheme.colors.primary10 },
        background = { MomensTheme.colors.primary50 },
    ),
    WHITE(
        textStyle = { MomensTheme.typography.bodyM12 },
        textColor = { MomensTheme.colors.gray600 },
        background = { MomensTheme.colors.white },
    ),
}
