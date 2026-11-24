package com.momens.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.type.MomensChipButtonType
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensChipButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    count: Int? = null,
    type: MomensChipButtonType = MomensChipButtonType.WHITE,
) {
    val backgroundColor = type.background()
    val contentColor = type.textColor()
    val typographyStyle = type.textStyle()

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = typographyStyle,
                color = contentColor,
            )
            count?.takeIf { it > 0 }?.let {
                Text(
                    text = it.toString(),
                    style = typographyStyle,
                    color = contentColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF666E74)
@Composable
private fun MomensChipButtonPreview() {
    MomensTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MomensChipButton(
                label = "All",
                count = 0,
                type = MomensChipButtonType.WHITE,
                onClick = {},
            )
            MomensChipButton(
                label = "All",
                count = 2,
                type = MomensChipButtonType.BLACK,
                onClick = {},
            )
            MomensChipButton(
                label = "Decision",
                count = 2,
                type = MomensChipButtonType.PURPLE,
                onClick = {},
            )
            MomensChipButton(
                label = "Risk",
                count = 2,
                type = MomensChipButtonType.RED,
                onClick = {},
            )
            MomensChipButton(
                label = "Change",
                count = 2,
                type = MomensChipButtonType.YELLOW,
                onClick = {},
            )
            MomensChipButton(
                label = "Question",
                count = 2,
                type = MomensChipButtonType.MINT,
                onClick = {},
            )
            MomensChipButton(
                label = "All",
                type = MomensChipButtonType.CLICK,
                onClick = {},
            )
            MomensChipButton(
                label = "All",
                type = MomensChipButtonType.DEFAULT,
                onClick = {},
            )
        }
    }
}
