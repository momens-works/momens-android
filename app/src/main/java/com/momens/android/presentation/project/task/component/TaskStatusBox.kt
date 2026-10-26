package com.momens.android.presentation.project.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.designsystem.component.sectiontitle.MomensSectionTitle
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.ImportantTone
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.project.task.model.MomensTaskButtonType
import com.momens.android.presentation.project.task.model.TaskItemData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TaskStatusBox(
    type: MomensStatusEditType,
    tasks: ImmutableList<TaskItemData>,
    onTaskClick: (TaskItemData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        MomensSectionTitle(
            title = type.label,
            count = tasks.size.toString(),
            isEmphasized = true,
            iconRes = type.iconRes,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tasks.forEach { task ->
                TaskListItem(
                    text = task.title,
                    label = task.role?.text,
                    level = task.priority,
                    tone = task.tone,
                    count = task.materialCount.toString(),
                    onClick = { onTaskClick(task) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskStatusBoxPreview() {
    MomensTheme {

        val dummyTasks = persistentListOf(
            TaskItemData(
                id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                title = "text",
                role = MomensTaskButtonType.FRONTEND,
                priority = ImportantLevel.LOW,
                materialCount = 4,
                tone = ImportantTone.WHITE,
            ),
            TaskItemData(
                id = "6f9c1a2b-8e3d-4f5a-9b1c-1d2e3f4a5b6c",
                title = "text",
                role = MomensTaskButtonType.FRONTEND,
                priority = ImportantLevel.HIGH,
                materialCount = 3,
                tone = ImportantTone.WHITE,
            ),
        )

        Box(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp,
                ),
        ) {
            Column {
                TaskStatusBox(
                    type = MomensStatusEditType.TODO,
                    tasks = dummyTasks,
                    onTaskClick = {},
                )

                Spacer(modifier = Modifier.height(20.dp))

                TaskStatusBox(
                    type = MomensStatusEditType.IN_PROGRESS,
                    tasks = persistentListOf(
                        TaskItemData(
                            id = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                            title = "text",
                            role = MomensTaskButtonType.FRONTEND,
                            priority = ImportantLevel.MEDIUM,
                            materialCount = 2,
                            tone = ImportantTone.WHITE,
                        ),
                    ),
                    onTaskClick = {},
                )

                Spacer(modifier = Modifier.height(20.dp))

                TaskStatusBox(
                    type = MomensStatusEditType.DONE,
                    tasks = persistentListOf(),
                    onTaskClick = {},
                )
            }
        }
    }
}
