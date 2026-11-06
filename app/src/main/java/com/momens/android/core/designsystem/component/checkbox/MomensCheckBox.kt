package com.momens.android.core.designsystem.component.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleToggleable
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensCheckBox(
    isChecked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isToggleEnabled: Boolean = true,
) {
    val iconRes = if (isChecked) R.drawable.ic_checkbox_fill else R.drawable.ic_checkbox_empty
    val iconTint = if (isChecked && enabled) MomensTheme.colors.primary50 else MomensTheme.colors.gray300
    val labelColor = if (enabled) MomensTheme.colors.gray800 else MomensTheme.colors.gray300

    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleToggleable(
                value = isChecked,
                onValueChange = onCheckedChange,
                enabled = enabled && isToggleEnabled,
                role = Role.Checkbox,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp),
        )

        Text(
            text = label,
            style = MomensTheme.typography.bodyM12,
            color = labelColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MomensCheckBoxPreview() {
    MomensTheme {
        var isFirstChecked by remember { mutableStateOf(false) }
        var isSecondChecked by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MomensCheckBox(
                isChecked = isFirstChecked,
                label = "어쩌구어쩌구 반영",
                onCheckedChange = { isFirstChecked = it },
            )

            MomensCheckBox(
                isChecked = isSecondChecked,
                label = "어쩌구어쩌구 반영",
                onCheckedChange = { isSecondChecked = it },
            )

            // 체크 기준이 아직 없을 때(default) 상태
            MomensCheckBox(
                isChecked = false,
                label = "어쩌구어쩌구 반영",
                onCheckedChange = {},
                enabled = false,
            )
        }
    }
}
