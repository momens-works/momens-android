package com.momens.android.presentation.project.taskdetail.model

import androidx.compose.runtime.Immutable
import com.momens.android.core.common.extension.limitLength
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.data.project.taskdetail.model.TaskPriorityModel
import com.momens.android.data.project.taskdetail.model.TaskRoleModel
import com.momens.android.data.project.taskdetail.model.TaskStatusModel
import com.momens.android.presentation.project.model.TaskRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.momens.android.data.project.taskdetail.model.TaskDetailModel as TaskDetailDataModel

@Immutable
data class TaskDetailModel(
    val id: String,
    val projectId: String,
    val title: String,
    val status: MomensStatusEditType,
    val role: TaskRole?,
    val assignee: TaskDetailAssigneeModel?,
    val priority: ImportantLevel,
    val purpose: String?,
    val checklist: TaskDetailChecklistModel,
    val materials: ImmutableList<TaskDetailFileModel>,
    val openQuestions: ImmutableList<TaskDetailQuestionModel>,
    val nextAction: String?,
)

private const val TITLE_MAX_LENGTH = 15
private const val PURPOSE_MAX_LENGTH = 300
private const val NEXT_ACTION_MAX_LENGTH = 100

fun TaskDetailDataModel.toUiModel(): TaskDetailModel = TaskDetailModel(
    id = id,
    projectId = projectId,
    title = title.limitLength(TITLE_MAX_LENGTH),
    status = status.toUiType(),
    role = role?.toUiType(),
    assignee = assignee?.toUiModel(),
    priority = priority.toUiType(),
    purpose = purpose?.limitLength(PURPOSE_MAX_LENGTH),
    checklist = checklist.toUiModel(),
    materials = materials.map { it.toUiModel() }.toImmutableList(),
    openQuestions = openQuestions.map { it.toUiModel() }.toImmutableList(),
    nextAction = nextAction?.limitLength(NEXT_ACTION_MAX_LENGTH),
)

private fun TaskStatusModel.toUiType(): MomensStatusEditType = when (this) {
    TaskStatusModel.BACKLOG -> MomensStatusEditType.BACKLOG
    TaskStatusModel.TODO -> MomensStatusEditType.TODO
    TaskStatusModel.IN_PROGRESS -> MomensStatusEditType.IN_PROGRESS
    TaskStatusModel.DONE -> MomensStatusEditType.DONE
    TaskStatusModel.CANCELLED -> MomensStatusEditType.CANCELED
}

private fun TaskPriorityModel.toUiType(): ImportantLevel = when (this) {
    TaskPriorityModel.LOW -> ImportantLevel.LOW
    TaskPriorityModel.MEDIUM -> ImportantLevel.MEDIUM
    TaskPriorityModel.HIGH -> ImportantLevel.HIGH
    TaskPriorityModel.URGENT -> ImportantLevel.HIGH
}

private fun TaskRoleModel.toUiType(): TaskRole = when (this) {
    TaskRoleModel.PM -> TaskRole.PM
    TaskRoleModel.DESIGN -> TaskRole.DESIGN
    TaskRoleModel.FRONTEND -> TaskRole.FRONTEND
    TaskRoleModel.BACKEND -> TaskRole.BACKEND
}
