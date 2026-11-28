package com.momens.android.presentation.signal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.signal.model.SignalTabType

@Composable
fun SignalTab(
    title: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val type = if (isSelected) SignalTabType.ACTIVE else SignalTabType.DEFAULT
    val lineColor = type.lineColor()

    Row(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth,
                )
            }
            .padding(horizontal = type.horizontalPadding, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            color = type.contentColor(),
            style = type.textStyle(),
        )

        Text(
            text = count.toString(),
            color = type.contentColor(),
            style = type.textStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignalTabPreview() {
    MomensTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SignalTab(
                title = "확인 필요",
                count = 12,
                isSelected = true,
                onClick = {},
            )
            SignalTab(
                title = "검토 필요",
                count = 1,
                isSelected = false,
                onClick = {},
            )
        }
    }
}
