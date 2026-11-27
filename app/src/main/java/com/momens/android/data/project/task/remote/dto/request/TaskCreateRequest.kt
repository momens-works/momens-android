package com.momens.android.data.project.task.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskCreateRequest(
    @SerialName("title") val title: String,
    @SerialName("roles") val roles: List<String>,
    @SerialName("priority") val priority: String,
)
