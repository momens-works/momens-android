package com.momens.android.core.designsystem.component.switch

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.momens.android.core.designsystem.component.dot.MomensDot
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensSwitch(
    isToggle: Boolean,
    modifier: Modifier = Modifier,
) {
    val dotOffset by animateDpAsState(
        targetValue = if (isToggle) 16.dp else 0.dp,
        label = "switchDot",
    )

    Row(
        modifier = modifier
            .width(36.dp)
            .background(
                color = MomensTheme.colors.primary50,
                shape = CircleShape
            )
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MomensDot(
            size = 16.dp,
            color = MomensTheme.colors.white,
            modifier = Modifier.offset(x = dotOffset),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MomensSwitchPreview() {
    var isToggle by remember { mutableStateOf(false) }

    MomensTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MomensSwitch(
                isToggle = isToggle,
                modifier = Modifier
                    .padding(16.dp)
                    .noRippleClickable { isToggle = !isToggle },
            )

            MomensSwitch(
                isToggle = !isToggle,
                modifier = Modifier
                    .padding(16.dp)
                    .noRippleClickable { isToggle = !isToggle },
            )
        }
    }
}
