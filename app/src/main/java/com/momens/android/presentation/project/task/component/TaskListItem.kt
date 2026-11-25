package com.momens.android.presentation.project.task.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.importantstatus.MomensImportantStatus
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.ImportantTone
import com.momens.android.core.designsystem.effect.momensUiShadow
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun TaskListItem(
    text: String,
    label: String?,
    level: ImportantLevel,
    tone: ImportantTone,
    count: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .momensUiShadow()
            .background(
                color = MomensTheme.colors.white,
                shape = RoundedCornerShape(8.dp),
            )
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = text,
                color = MomensTheme.colors.gray900,
                style = MomensTheme.typography.bodyB14,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                label?.let {
                    TaskListItemLabel(label = it)
                }

                MomensImportantStatus(level = level, tone = tone)

                TaskListItemCount(
                    count = count
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            tint = MomensTheme.colors.black,
        )
    }
}

@Composable
private fun TaskListItemLabel(
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = MomensTheme.colors.primary10,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),

        ) {
        Text(
            text = label,
            color = MomensTheme.colors.gray700,
            style = MomensTheme.typography.bodyM12,
        )
    }
}

@Composable
private fun TaskListItemCount(
    count: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_clip),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MomensTheme.colors.gray600,
        )

        Text(
            text = count,
            color = MomensTheme.colors.gray600,
            style = MomensTheme.typography.bodyM12,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListItemPreview() {
    MomensTheme {
        Box(
            modifier = Modifier
                .background(color = MomensTheme.colors.black)
                .padding(10.dp),

            ) {
            TaskListItem(
                text = "text",
                label = "Android",
                level = ImportantLevel.LOW,
                tone = ImportantTone.WHITE,
                count = "2",
                onClick = {},
            )
        }
    }
}
