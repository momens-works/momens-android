package com.momens.android.data.project.taskdetail.model

import com.momens.android.data.project.taskdetail.remote.dto.response.TaskDetailResponse

data class TaskDetailModel(
    val id: String,
    val projectId: String,
    val title: String,
    val status: TaskStatusModel,
    val roles: List<TaskRoleModel>,
    val assignee: TaskAssigneeModel?,
    val priority: TaskPriorityModel,
    val purpose: String?,
    val checklist: TaskChecklistModel,
    val materials: List<TaskMaterialModel>,
    val openQuestions: List<TaskOpenQuestionModel>,
    val nextAction: String?,
)

fun TaskDetailResponse.toModel(): TaskDetailModel = TaskDetailModel(
    id = id,
    projectId = projectId,
    title = title,
    status = status.toModel(),
    roles = roles.map { it.toModel() },
    assignee = assignee?.toModel(),
    priority = priority.toModel(),
    purpose = purpose,
    checklist = checklist.toModel(),
    materials = materials.map { it.toModel() },
    openQuestions = openQuestions.map { it.toModel() },
    nextAction = nextAction,
)
