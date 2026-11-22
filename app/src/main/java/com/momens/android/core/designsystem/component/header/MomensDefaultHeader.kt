package com.momens.android.core.designsystem.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensDefaultHeader(
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    avatarUrl: String? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 10.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_momens_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(100.dp)
                .aspectRatio(90f / 20f),
            tint = Color.Unspecified,
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(30.dp)
                .background(
                    shape = CircleShape,
                    color = MomensTheme.colors.primary50,
                )
                .noRippleClickable(onClick = onProfileClick),
        ) {
            if (avatarUrl == null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = MomensTheme.colors.white,
                )
            } else {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_person),
                    error = painterResource(id = R.drawable.ic_person),
                    fallback = painterResource(id = R.drawable.ic_person),
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MomensDefaultHeaderPreview() {
    MomensTheme {
        Column {
            MomensDefaultHeader(
                onProfileClick = { },
            )
        }
    }
}
