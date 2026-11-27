package com.momens.android.presentation.project.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.bottomsheet.MomensBottomSheet
import com.momens.android.core.designsystem.component.button.MomensButton
import com.momens.android.core.designsystem.component.button.MomensCtaButton
import com.momens.android.core.designsystem.component.importantstatus.MomensImportantStatus
import com.momens.android.core.designsystem.component.input.MomensCountInput
import com.momens.android.core.designsystem.component.snackbar.MomensSnackbar
import com.momens.android.core.designsystem.component.snackbar.model.MomensSnackbarModel
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.ImportantTone
import com.momens.android.core.designsystem.component.type.MomensButtonType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.project.task.model.MomensTaskButtonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(
    isCreatingTask: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onRegisterClick: (title: String, role: MomensTaskButtonType, priority: ImportantLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleState = rememberTextFieldState()
    var selectedRole by remember { mutableStateOf<MomensTaskButtonType?>(null) }
    var selectedPriority by remember { mutableStateOf<ImportantLevel?>(null) }
    val roles = MomensTaskButtonType.entries
    val priorities = ImportantLevel.entries

    val isButtonEnabled = titleState.text.isNotBlank() &&
        selectedRole != null && selectedPriority != null && !isCreatingTask

    MomensBottomSheet(
        onDismiss = onDismiss,
        dismissEnabled = !isCreatingTask,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 32.dp,
            ),
        ) {
            item {
                Text(
                    text = "새 태스크 생성",
                    color = MomensTheme.colors.black,
                    style = MomensTheme.typography.bodyB16,
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "제목",
                    color = MomensTheme.colors.gray600,
                    style = MomensTheme.typography.bodyB14,
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                MomensCountInput(
                    state = titleState,
                    placeholder = "Title",
                    maxLength = 15,
                    lineLimits = TextFieldLineLimits.SingleLine,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "역할",
                    style = MomensTheme.typography.bodyB14,
                    color = MomensTheme.colors.gray600,
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    roles.forEach { role ->
                        val isSelected = selectedRole == role
                        MomensButton(
                            text = role.text,
                            onClick = { selectedRole = role },
                            type = if (isSelected) MomensButtonType.PRIMARY
                            else MomensButtonType.GRAY,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "우선순위",
                    style = MomensTheme.typography.bodyB14,
                    color = MomensTheme.colors.gray600,
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    priorities.forEach { level ->
                        val isSelected = selectedPriority == level
                        val tone = if (isSelected) ImportantTone.BLUE else ImportantTone.GRAY

                        MomensImportantStatus(
                            level = level,
                            tone = tone,
                            modifier = Modifier.noRippleClickable { selectedPriority = level },
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                MomensCtaButton(
                    onClick = {
                        onRegisterClick(
                            titleState.text.toString(),
                            requireNotNull(selectedRole),
                            requireNotNull(selectedPriority),
                        )
                    },
                    enabled = isButtonEnabled,
                ) {
                    Text(
                        text = if (isCreatingTask) "등록 중..." else "태스크 등록",
                        color = MomensTheme.colors.white,
                        style = MomensTheme.typography.bodyB16,
                    )
                }
            }

            if (errorMessage != null) {
                item {
                    MomensSnackbar(
                        content = MomensSnackbarModel(title = errorMessage),
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TaskBottomSheetPreview() {
    MomensTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TaskBottomSheet(
                isCreatingTask = false,
                errorMessage = null,
                onDismiss = {},
                onRegisterClick = { _, _, _ -> },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TaskBottomSheetErrorPreview() {
    MomensTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TaskBottomSheet(
                isCreatingTask = false,
                errorMessage = "태스크 등록에 실패했습니다",
                onDismiss = {},
                onRegisterClick = { _, _, _ -> },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TaskBottomSheetCreatingPreview() {
    MomensTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TaskBottomSheet(
                isCreatingTask = true,
                errorMessage = null,
                onDismiss = {},
                onRegisterClick = { _, _, _ -> },
            )
        }
    }
}
