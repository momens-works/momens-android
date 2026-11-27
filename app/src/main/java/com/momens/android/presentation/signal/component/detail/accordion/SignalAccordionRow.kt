package com.momens.android.presentation.signal.component.detail.accordion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.common.extension.ellipsize
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.signal.model.SignalAccordionItem

private const val VALUE_MAX_LENGTH = 40

@Composable
fun SignalAccordionRow(
    item: SignalAccordionItem,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = item.title,
            style = MomensTheme.typography.bodyB12,
            color = MomensTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = item.value.ellipsize(VALUE_MAX_LENGTH),
            style = MomensTheme.typography.bodyM12,
            color = MomensTheme.colors.gray500,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignalAccordionRowPreview() {
    MomensTheme {
        SignalAccordionRow(
            item = SignalAccordionItem(title = "대상", value = "권한 요청 화면"),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignalAccordionRowBoundaryPreview() {
    MomensTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 40자: 그대로 표시
            SignalAccordionRow(
                item = SignalAccordionItem(title = "변화", value = "가".repeat(40)),
            )
            // 41자: 앞 39자 + …
            SignalAccordionRow(
                item = SignalAccordionItem(title = "영향", value = "가".repeat(41)),
            )
        }
    }
}
