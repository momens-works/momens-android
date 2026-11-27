package com.momens.android.data.project.taskedit.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskEditRequestDto(
    @SerialName("title") val title: String,
    @SerialName("roles") val roles: List<String>,
    @SerialName("assignee_id") val assigneeId: String?,
    @SerialName("priority") val priority: String,
    @SerialName("status") val status: String,
    @SerialName("purpose") val purpose: String,
    @SerialName("checklist_items") val checklistItems: List<ChecklistItems>,
)

@Serializable
data class ChecklistItems(
    @SerialName("id") val id: String?,
    @SerialName("title") val title: String,
    @SerialName("completed") val completed: Boolean,
)
