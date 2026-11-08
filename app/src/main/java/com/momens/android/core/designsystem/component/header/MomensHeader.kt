package com.momens.android.core.designsystem.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensHeader(
    text: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MomensTheme.colors.white,
    isSaveVisible: Boolean = false,
    isWriteVisible: Boolean = false,
    onWriteClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    isSaveEnabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 19.dp, vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            tint = MomensTheme.colors.gray500,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .scale(scaleX = -1f, scaleY = 1f)
                .noRippleClickable(onClick = onBackClick)
        )

        Text(
            text = text,
            style = MomensTheme.typography.bodyM16,
            color = MomensTheme.colors.black,
            modifier = Modifier.align(Alignment.Center)
        )

        if (isWriteVisible) {
            Icon(
                painter = painterResource(id = R.drawable.ic_write),
                contentDescription = null,
                tint = MomensTheme.colors.gray500,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .noRippleClickable(onClick = onWriteClick)
            )
        }

        if (isSaveVisible) {
            Text(
                text = "완료",
                style = MomensTheme.typography.bodyB14,
                color = MomensTheme.colors.gray900,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .noRippleClickable(
                        enabled = isSaveEnabled,
                        onClick = onSaveClick
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MomensHeaderPreview() {
    MomensTheme {
        Column {
            MomensHeader(
                text = "파자마 파티즈",
                onBackClick = {},
                onWriteClick = {}
            )

            MomensHeader(
                text = "우우와와우와우와와 타쿠타쿠 챠오",
                onBackClick = {},
                isWriteVisible = true,
                onWriteClick = {}
            )

            MomensHeader(
                text = "우우와와우와우와와 타쿠타쿠 챠오",
                onBackClick = {},
                isSaveVisible = true,
                onSaveClick = {}
            )
        }
    }
}
