package com.momens.android.data.project.model

private val taskRoleOrder = listOf("pm", "design", "backend", "frontend")

fun List<String>.toTaskRequestRoles(): List<String> {
    require(isNotEmpty()) { "At least one task role is required." }
    require(all { it in taskRoleOrder }) { "Unsupported task role." }

    return taskRoleOrder.filter { it in this }
}
