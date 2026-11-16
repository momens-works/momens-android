package com.momens.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.type.MomensCtaType
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensCtaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: MomensCtaType = MomensCtaType.DEFAULT,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val backgroundColor = if (!enabled) {
        MomensTheme.colors.gray300
    } else {
        when (type) {
            MomensCtaType.DEFAULT -> MomensTheme.colors.primary100
            MomensCtaType.LOGIN, MomensCtaType.RESET -> MomensTheme.colors.white
            MomensCtaType.CANCEL -> MomensTheme.colors.gray300
            MomensCtaType.DELETE -> MomensTheme.colors.white
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .noRippleClickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }

}

@Preview(widthDp = 320)
@Composable
private fun MomensCtaButtonPreview() {
    MomensTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            MomensCtaButton(onClick = {}) {
                Text(
                    text = "text",
                    color = MomensTheme.colors.white,
                    style = MomensTheme.typography.bodyB16,
                )
            }

            MomensCtaButton(
                onClick = {},
                enabled = false,
            ) {
                Text(
                    text = "text",
                    color = MomensTheme.colors.white,
                    style = MomensTheme.typography.bodyB16,
                )
            }

            MomensCtaButton(
                onClick = {},
                type = MomensCtaType.CANCEL,
            ) {
                Text(
                    text = "text",
                    color = MomensTheme.colors.gray700,
                    style = MomensTheme.typography.bodyB16,
                )
            }

            MomensCtaButton(
                onClick = {},
                type = MomensCtaType.LOGIN,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                    Text(
                        text = "text",
                        color = MomensTheme.colors.gray700,
                        style = MomensTheme.typography.bodyB16,
                    )
                }
            }
        }
    }
}
