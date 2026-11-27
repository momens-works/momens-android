package com.momens.android.core.designsystem.component.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensCountInput(
    state: TextFieldState,
    maxLength: Int,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
) {
    val length = state.text.length
    val counterColor = when {
        length == 0 -> MomensTheme.colors.gray300
        length >= maxLength -> MomensTheme.colors.pointRed
        else -> MomensTheme.colors.gray400
    }

    MomensInput(
        state = state,
        modifier = modifier,
        placeholder = placeholder,
        lineLimits = lineLimits,
        maxLength = maxLength,
        trailingContent = {
            Text(
                text = "$length/$maxLength",
                style = MomensTheme.typography.bodyM12,
                color = counterColor,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun MomensCountInputPreview() {
    MomensTheme {
        val emptyState = rememberTextFieldState()
        val normalState = rememberTextFieldState(initialText = "Title")
        val maxState = rememberTextFieldState(initialText = "Maximum length!")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MomensCountInput(
                state = emptyState,
                placeholder = "Title",
                maxLength = 15,
            )

            MomensCountInput(
                state = normalState,
                placeholder = "Title",
                maxLength = 15,
            )

            MomensCountInput(
                state = maxState,
                placeholder = "Title",
                maxLength = 15,
            )
        }
    }
}
