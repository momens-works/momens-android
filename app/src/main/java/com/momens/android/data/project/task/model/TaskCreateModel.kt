package com.momens.android.data.project.task.model

import com.momens.android.data.project.model.toTaskRequestRoles
import com.momens.android.data.project.task.remote.dto.request.TaskCreateRequest

data class TaskCreateModel(
    val title: String,
    val roles: List<String>,
    val priority: String,
)

fun TaskCreateModel.toDto(): TaskCreateRequest = TaskCreateRequest(
    title = title,
    roles = roles.toTaskRequestRoles(),
    priority = priority,
)
