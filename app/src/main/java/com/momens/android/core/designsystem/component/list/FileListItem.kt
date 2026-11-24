package com.momens.android.core.designsystem.component.list

import androidx.annotation.DrawableRes
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
import com.momens.android.core.common.extension.ellipsize
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.dot.MomensDot
import com.momens.android.core.designsystem.theme.MomensTheme

private const val CATEGORY_MAX_LENGTH = 30

@Composable
fun MomensFileListItem(
    title: String,
    role: String,
    category: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int = R.drawable.ic_file,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .background(
                color = MomensTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FileIcon(iconResId = iconResId)

            FileTextContent(
                title = title,
                role = role,
                category = category,
                modifier = Modifier.weight(1f),
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MomensTheme.colors.black,
        )
    }
}

@Composable
private fun FileIcon(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = MomensTheme.colors.white,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 8.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MomensTheme.colors.primary50,
        )
    }
}

@Composable
private fun FileTextContent(
    title: String,
    role: String,
    category: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = title,
            color = MomensTheme.colors.black,
            style = MomensTheme.typography.bodyB12,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = role,
                color = MomensTheme.colors.gray400,
                style = MomensTheme.typography.captionM11,
            )

            MomensDot(
                color = MomensTheme.colors.gray400,
            )

            Text(
                text = category.ellipsize(CATEGORY_MAX_LENGTH),
                color = MomensTheme.colors.gray400,
                style = MomensTheme.typography.captionM11,
            )
        }
    }
}

@Preview
@Composable
private fun MomensFileListItemPreview() {
    MomensTheme {
        MomensFileListItem(
            title = "회원가입 에러 메시지 정책 초안",
            role = "PM",
            category = "copy policy",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun MomensFileListItemCategoryBoundaryPreview() {
    MomensTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 30자: 그대로 표시
            MomensFileListItem(
                title = "회원가입 에러 메시지 정책 초안",
                role = "PM",
                category = "a".repeat(30),
                onClick = {},
            )
            // 31자: 앞 29자 + …
            MomensFileListItem(
                title = "회원가입 에러 메시지 정책 초안",
                role = "PM",
                category = "a".repeat(31),
                onClick = {},
            )
        }
    }
}
