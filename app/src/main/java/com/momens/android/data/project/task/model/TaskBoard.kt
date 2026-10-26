package com.momens.android.data.project.task.model

import com.momens.android.data.project.task.remote.dto.response.CreatedTaskResponse
import com.momens.android.data.project.task.remote.dto.response.TaskBoardResponse
import com.momens.android.data.project.task.remote.dto.response.TaskCreateResponse
import com.momens.android.data.project.task.remote.dto.response.TaskGroupResponse
import com.momens.android.data.project.task.remote.dto.response.TaskResponse

data class TaskBoard(
    val title: String,
    val description: String,
    val groups: List<TaskGroup>,
)

data class TaskGroup(
    val groupKey: String,
    val label: String,
    val count: Int,
    val tasks: List<TaskItem>,
)

data class TaskItem(
    val id: String,
    val title: String,
    val role: String?,
    val priority: String,
    val materialCount: Int,
)

fun TaskBoardResponse.toModel(): TaskBoard = TaskBoard(
    title = title,
    description = description,
    groups = groups.map { it.toModel() },
)

fun TaskGroupResponse.toModel(): TaskGroup = TaskGroup(
    groupKey = groupKey,
    label = label,
    count = count,
    tasks = tasks.map { it.toModel() },
)

fun TaskResponse.toModel(): TaskItem = TaskItem(
    id = id,
    title = title,
    role = role,
    priority = priority,
    materialCount = materialCount,
)

fun TaskCreateResponse.toModel(): TaskItem = task.toModel()

fun CreatedTaskResponse.toModel(): TaskItem = TaskItem(
    id = id,
    title = title,
    role = role,
    priority = priority,
    materialCount = 0,
)
