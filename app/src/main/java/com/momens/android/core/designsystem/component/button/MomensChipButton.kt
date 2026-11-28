package com.momens.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    count: Int? = null,
    selectedType: MomensChipButtonType = MomensChipButtonType.SELECTED,
    unselectedType: MomensChipButtonType = MomensChipButtonType.UNSELECTED,
) {
    val type = if (isSelected) selectedType else unselectedType

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

private val previewChips = listOf(
    "All" to MomensChipButtonType.BLACK,
    "Change" to MomensChipButtonType.YELLOW,
    "Decision" to MomensChipButtonType.PURPLE,
    "Question" to MomensChipButtonType.MINT,
    "Risk" to MomensChipButtonType.RED,
)

@Preview(showBackground = true, backgroundColor = 0xFF666E74)
@Composable
private fun MomensChipButtonDefaultPreview() {
    var selectedLabel by remember { mutableStateOf("All") }

    MomensTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            previewChips.forEach { (label, _) ->
                MomensChipButton(
                    label = label,
                    isSelected = label == selectedLabel,
                    onClick = { selectedLabel = label },
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF666E74)
@Composable
private fun MomensChipButtonCustomTypePreview() {
    var selectedLabel by remember { mutableStateOf("All") }

    MomensTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            previewChips.forEach { (label, selectedType) ->
                MomensChipButton(
                    label = label,
                    isSelected = label == selectedLabel,
                    count = 2,
                    selectedType = selectedType,
                    unselectedType = MomensChipButtonType.WHITE,
                    onClick = { selectedLabel = label },
                )
            }
        }
    }
}
