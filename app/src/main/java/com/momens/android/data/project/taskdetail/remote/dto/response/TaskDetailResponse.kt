package com.momens.android.data.project.taskdetail.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDetailResponse(
    @SerialName("id")
    val id: String,

    @SerialName("project_id")
    val projectId: String,

    @SerialName("title")
    val title: String,

    @SerialName("status")
    val status: TaskStatusDto,

    @SerialName("roles")
    val roles: List<TaskRoleDto>,

    @SerialName("assignee")
    val assignee: TaskAssigneeDto?,

    @SerialName("priority")
    val priority: TaskPriorityDto,

    @SerialName("purpose")
    val purpose: String?,

    @SerialName("checklist")
    val checklist: TaskChecklistDto,

    @SerialName("materials")
    val materials: List<TaskMaterialDto>,

    @SerialName("open_questions")
    val openQuestions: List<TaskOpenQuestionDto>,

    @SerialName("next_action")
    val nextAction: String?,
)
