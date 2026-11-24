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
import com.momens.android.presentation.signal.model.SignalTapType

@Composable
fun SignalTap(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: SignalTapType = SignalTapType.DEFAULT,
) {
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
            text = type.title,
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
private fun SignalTapPreview() {
    MomensTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SignalTap(
                count = 12,
                onClick = {},
                type = SignalTapType.ACTIVE,
            )
            SignalTap(
                count = 1,
                onClick = {},
                type = SignalTapType.DEFAULT,
            )
        }
    }
}
