package com.momens.android.data.project.task.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskBoardResponse(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("groups") val groups: List<TaskGroupResponse>,
)

@Serializable
data class TaskGroupResponse(
    @SerialName("group_key") val groupKey: String,
    @SerialName("label") val label: String,
    @SerialName("count") val count: Int,
    @SerialName("tasks") val tasks: List<TaskResponse>,
)

@Serializable
data class TaskResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("role") val role: String?,
    @SerialName("priority") val priority: String,
    @SerialName("material_count") val materialCount: Int,
)
