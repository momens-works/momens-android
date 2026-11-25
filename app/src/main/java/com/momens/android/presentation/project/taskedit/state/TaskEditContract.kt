package com.momens.android.presentation.project.taskedit.state

import androidx.compose.runtime.Immutable
import com.momens.android.presentation.project.model.Assignee
import com.momens.android.presentation.project.taskedit.model.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TaskEditState(
    val pageTitle: String = "태스크 수정",
    val task: Task,
    val assignees: ImmutableList<Assignee>,
    val isAssigneeSheetVisible: Boolean = false,
    val isStatusSheetVisible: Boolean = false,
    val isSaving: Boolean = false,
) {
    companion object {
        val Fake = TaskEditState(
            task = Task.Fake,
            assignees = persistentListOf(
                Assignee(id = "1", name = "강채원", url = null),
                Assignee(id = "2", name = "김민지", url = null),
                Assignee(id = "3", name = "이서준", url = null),
            ),
        )
    }
}

sealed interface TaskEditSideEffect {
    data class ShowSnackBar(val message: String) : TaskEditSideEffect
    data object NavigateUp : TaskEditSideEffect
}
