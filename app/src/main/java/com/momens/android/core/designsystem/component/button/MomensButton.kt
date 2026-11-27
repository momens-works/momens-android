package com.momens.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.type.MomensButtonType
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    type: MomensButtonType? = null,
) {
    val buttonType = type ?: if (isSelected) {
        MomensButtonType.PRIMARY
    } else {
        MomensButtonType.GRAY
    }
    val backgroundColor = buttonType.background()
    val contentColor = buttonType.textColor()
    val typographyStyle = buttonType.textStyle()

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .noRippleClickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                color = contentColor,
                style = typographyStyle,
            )

            if (buttonType.isDropdown) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(90f),
                    tint = contentColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFE8E8E)
@Composable
private fun MomensButtonPreview() {
    MomensTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MomensButton(text = "버튼", onClick = {})
            MomensButton(text = "선택", isSelected = true, onClick = {})
            MomensButton(text = "버튼", type = MomensButtonType.GRAY, onClick = {})
            MomensButton(text = "버튼", type = MomensButtonType.PRIMARY, onClick = {})
            MomensButton(text = "버튼", type = MomensButtonType.BLACK, onClick = {})
            MomensButton(text = "버튼", type = MomensButtonType.WHITE, onClick = {})
            MomensButton(text = "버튼", type = MomensButtonType.WHITE_DROPDOWN, onClick = {})
            MomensButton(text = "버튼", type = MomensButtonType.PRIMARY_DROPDOWN, onClick = {})
        }
    }
}
