package com.momens.android.core.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.momens.android.R
import com.momens.android.core.designsystem.component.button.MomensCtaButton
import com.momens.android.core.designsystem.component.type.MomensCtaType
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensModal(
    title: String,
    description: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    cancelText: String = "취소하기",
    confirmText: String = "삭제하기",
) {
    Column(
        modifier = modifier
            .background(color = MomensTheme.colors.white, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_warning),
            contentDescription = null,
            tint = MomensTheme.colors.gray300,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.height(height = 8.dp))

        Text(
            text = title,
            color = MomensTheme.colors.black,
            style = MomensTheme.typography.bodyB16,
        )

        Spacer(modifier = Modifier.height(height = 4.dp))

        Text(
            text = description,
            color = MomensTheme.colors.gray500,
            style = MomensTheme.typography.bodyM14,
        )

        Spacer(modifier = Modifier.height(height = 18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MomensCtaButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                type = MomensCtaType.CANCEL,
            ) {
                Text(
                    text = cancelText,
                    color = MomensTheme.colors.white,
                    style = MomensTheme.typography.bodyB16,
                )
            }

            MomensCtaButton(
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                type = MomensCtaType.DEFAULT,
            ) {
                Text(
                    text = confirmText,
                    color = MomensTheme.colors.white,
                    style = MomensTheme.typography.bodyB16,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MomensModalPreview() {
    MomensTheme {
        MomensModal(
            title = "해당 시그널을 삭제하시겠어요?",
            description = "삭제하면 복구할 수 없어요",
            onCancel = {},
            onConfirm = {},
        )
    }
}

@Preview
@Composable
private fun MomensModalSmallWidthPreview() {
    MomensTheme {
        Column(modifier = Modifier.width(320.dp)) {
            MomensModal(
                title = "해당 시그널을 삭제하시겠어요?",
                description = "삭제하면 복구할 수 없어요",
                onCancel = {},
                onConfirm = {},
            )
        }
    }
}
