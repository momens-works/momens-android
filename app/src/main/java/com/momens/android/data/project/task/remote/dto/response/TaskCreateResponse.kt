package com.momens.android.data.project.task.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskCreateResponse(
    @SerialName("task") val task: CreatedTaskResponse,
)

@Serializable
data class CreatedTaskResponse(
    @SerialName("id") val id: String,
    @SerialName("project_id") val projectId: String,
    @SerialName("title") val title: String,
    @SerialName("role") val role: String?,
    @SerialName("priority") val priority: String,
    @SerialName("status") val status: String,
)
