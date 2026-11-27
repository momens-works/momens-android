package com.momens.android.presentation.project.task

import androidx.compose.runtime.Immutable
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.ImportantTone
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.data.project.task.model.TaskBoard
import com.momens.android.presentation.project.task.model.MomensTaskButtonType
import com.momens.android.presentation.project.task.model.TaskItemData
import com.momens.android.presentation.project.task.model.TaskSectionUiModel
import com.momens.android.presentation.project.task.model.toUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class TaskUiState(
    val title: String = "",
    val description: String = "",
    val sections: ImmutableList<TaskSectionUiModel> = persistentListOf(),
) {
    companion object {
        private val fakeTasks = persistentListOf(
            TaskItemData(
                id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                title = "text",
                role = MomensTaskButtonType.FRONTEND,
                priority = ImportantLevel.LOW,
                materialCount = 2,
                tone = ImportantTone.WHITE,
            ),
            TaskItemData(
                id = "6f9c1a2b-8e3d-4f5a-9b1c-1d2e3f4a5b6c",
                title = "text",
                role = MomensTaskButtonType.FRONTEND,
                priority = ImportantLevel.LOW,
                materialCount = 2,
                tone = ImportantTone.WHITE,
            ),
            TaskItemData(
                id = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                title = "text",
                role = MomensTaskButtonType.FRONTEND,
                priority = ImportantLevel.LOW,
                materialCount = 2,
                tone = ImportantTone.WHITE,
            ),
        )

        val Fake = TaskUiState(
            title = "프로젝트 태스크",
            description = "업무를 한눈에 확인하고 상세 내용을 확인하세요.",
            sections = persistentListOf(
                TaskSectionUiModel(type = MomensStatusEditType.TODO, tasks = fakeTasks),
                TaskSectionUiModel(type = MomensStatusEditType.IN_PROGRESS, tasks = fakeTasks),
                TaskSectionUiModel(type = MomensStatusEditType.DONE, tasks = fakeTasks),
            ),
        )

        val FakeEmpty = TaskUiState(
            title = "프로젝트 태스크",
            description = "업무를 한눈에 확인하고 상세 내용을 확인하세요.",
            sections = persistentListOf(
                TaskSectionUiModel(type = MomensStatusEditType.TODO),
                TaskSectionUiModel(type = MomensStatusEditType.IN_PROGRESS),
                TaskSectionUiModel(type = MomensStatusEditType.DONE),
            ),
        )
    }
}

fun TaskBoard.toUiModel(): TaskUiState = TaskUiState(
    title = title,
    description = description,
    sections = groups.map { it.toUiModel() }.toPersistentList(),
)

sealed interface TaskSideEffect {
    data class TaskCreated(
        val message: String,
        val description: String,
        val taskId: String,
    ) : TaskSideEffect

    data class ShowSnackbar(val message: String) : TaskSideEffect

    data class TaskCreationFailed(
        val projectId: String,
        val message: String,
    ) : TaskSideEffect

    data class NavigateToTaskDetail(val taskId: String) : TaskSideEffect
}
