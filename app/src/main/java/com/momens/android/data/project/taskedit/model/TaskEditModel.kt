package com.momens.android.data.project.taskedit.model

import com.momens.android.data.project.model.toTaskRequestRoles
import com.momens.android.data.project.taskedit.remote.dto.request.ChecklistItems
import com.momens.android.data.project.taskedit.remote.dto.request.TaskEditRequestDto

data class TaskEditModel(
    val title: String,
    val roles: List<String>,
    val assigneeId: String?,
    val priority: String,
    val status: String,
    val purpose: String,
    val checklistItems: List<ChecklistItemsModel>,
)

data class ChecklistItemsModel(
    val id: String?,
    val title: String,
    val completed: Boolean,
)

fun TaskEditModel.toRequest(): TaskEditRequestDto =
    TaskEditRequestDto(
        title = title,
        roles = roles.toTaskRequestRoles(),
        assigneeId = assigneeId,
        priority = priority,
        status = status,
        purpose = purpose,
        checklistItems = checklistItems.map {
            ChecklistItems(id = it.id, title = it.title, completed = it.completed)
        },
    )
