package com.momens.android.core.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.button.MomensButton
import com.momens.android.core.designsystem.component.snackbar.model.MomensSnackbarModel
import com.momens.android.core.designsystem.component.type.MomensButtonType
import com.momens.android.core.designsystem.component.type.MomensSnackbarType
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensSnackbar(
    content: MomensSnackbarModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MomensTheme.colors.gray700,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(content.type.iconRes),
            contentDescription = null,
            tint = MomensTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.width(12.dp))

        when (content.type) {
            MomensSnackbarType.DEFAULT -> {
                Text(
                    text = content.title,
                    style = MomensTheme.typography.bodyB14,
                    color = MomensTheme.colors.white,
                )
            }

            MomensSnackbarType.BUTTON -> {
                MomensSnackbarTextColumn(
                    title = content.title,
                    description = content.description,
                    modifier = Modifier.weight(1f),
                )

                Box(
                    modifier = Modifier
                        .heightIn(min = 30.dp)
                        .noRippleClickable(onClick = content.onActionClick),
                    contentAlignment = Alignment.Center,
                ) {
                    MomensButton(
                        text = "바로보기",
                        onClick = content.onActionClick,
                        type = MomensButtonType.PRIMARY,
                    )
                }
            }
        }
    }
}

@Composable
private fun MomensSnackbarTextColumn(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MomensTheme.typography.bodyB14,
            color = MomensTheme.colors.white,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = description,
            style = MomensTheme.typography.captionM11,
            color = MomensTheme.colors.gray300,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MomensSnackbarPreview() {
    MomensTheme {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .padding(horizontal = 20.dp),
        ) {
            MomensSnackbar(
                content = MomensSnackbarModel(
                    title = "text",
                    description = "text",
                    type = MomensSnackbarType.BUTTON,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            MomensSnackbar(
                content = MomensSnackbarModel(title = "text"),
            )
        }
    }
}
