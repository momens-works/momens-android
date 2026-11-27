package com.momens.android.data.project

import com.momens.android.data.project.task.model.TaskCreateModel
import com.momens.android.data.project.task.model.toDto
import com.momens.android.data.project.task.remote.dto.response.TaskBoardResponse
import com.momens.android.data.project.task.remote.dto.response.TaskCreateResponse
import com.momens.android.data.project.taskdetail.remote.dto.response.TaskDetailResponse
import com.momens.android.data.project.taskedit.model.TaskEditModel
import com.momens.android.data.project.taskedit.model.toRequest
import com.momens.android.presentation.project.model.TaskRole
import com.momens.android.presentation.project.task.model.MomensTaskButtonType
import com.momens.android.presentation.project.taskedit.model.toEditTask
import com.momens.android.presentation.project.taskedit.model.toTaskEditModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import com.momens.android.data.project.task.model.toModel as toTaskModel
import com.momens.android.data.project.taskdetail.model.toModel as toDetailModel
import com.momens.android.presentation.project.task.model.toUiModel as toTaskUiModel
import com.momens.android.presentation.project.task.toUiModel as toBoardUiModel
import com.momens.android.presentation.project.taskdetail.model.toUiModel as toDetailUiModel

class TaskApiContractTest {
    @Test
    fun boardLoadsWithoutDescriptionOrLegacyRoleAndPreservesAllRoles() {
        val response = Json.decodeFromString<TaskBoardResponse>(
            """
            {
              "title": "프로젝트 태스크",
              "groups": [{
                "group_key": "todo", "label": "투두", "count": 2,
                "tasks": [
                  {"id": "1", "title": "복수 역할", "roles": ["pm", "design", "backend", "frontend"],
                   "priority": "low", "material_count": 0},
                  {"id": "2", "title": "역할 없음", "roles": [], "priority": "low", "material_count": 0}
                ]
              }]
            }
            """.trimIndent(),
        )

        val tasks = response.toTaskModel().toBoardUiModel().sections.single().tasks

        assertEquals(
            listOf(
                MomensTaskButtonType.PM,
                MomensTaskButtonType.DESIGN,
                MomensTaskButtonType.BACKEND,
                MomensTaskButtonType.FRONTEND,
            ),
            tasks.first().roles,
        )
        assertTrue(tasks.last().roles.isEmpty())
    }

    @Test
    fun createdTaskMapsMultipleAndEmptyRolesWithoutLegacyRole() {
        listOf(listOf("design", "backend"), emptyList()).forEach { roles ->
            val response = Json.decodeFromString<TaskCreateResponse>(
                """
                {"task": {"id": "1", "project_id": "project", "title": "태스크",
                 "roles": ${Json.encodeToString(roles)}, "priority": "low", "status": "todo"}}
                """.trimIndent(),
            )

            val task = response.toTaskModel().toTaskUiModel()

            assertEquals(roles, task.roles.map { it.name.lowercase() })
        }
    }

    @Test
    fun unchangedRolePreservesTheEntireServerListAndExplicitSelectionReplacesIt() {
        val detail = Json.decodeFromString<TaskDetailResponse>(
            detailJson("""["pm", "design", "backend", "frontend"]"""),
        )
            .toDetailModel()
            .toDetailUiModel()
        val editTask = detail.toEditTask()

        assertEquals(listOf(TaskRole.PM, TaskRole.DESIGN, TaskRole.BACKEND, TaskRole.FRONTEND), editTask.originalRoles)
        assertEquals(
            listOf("pm", "design", "backend", "frontend"),
            editTask.copy(titleState = "제목만 수정").toTaskEditModel().toRequest().roles,
        )

        val request = editTask.copy(role = TaskRole.BACKEND)
            .toTaskEditModel()
            .toRequest()

        assertEquals(listOf("backend"), request.roles)
    }

    @Test
    fun emptyDetailRolesRemainEmptyInsteadOfDefaultingToPm() {
        val editTask = Json.decodeFromString<TaskDetailResponse>(detailJson("[]"))
            .toDetailModel()
            .toDetailUiModel()
            .toEditTask()

        assertTrue(editTask.originalRoles.isEmpty())
        assertTrue(editTask.toTaskEditModel().roles.isEmpty())
    }

    @Test
    fun createRequestWritesOnlyUniqueAllowedRolesInServerOrder() {
        val request = TaskCreateModel("태스크", listOf("frontend", "pm", "frontend", "backend"), "low").toDto()
        val json = Json.parseToJsonElement(Json.encodeToString(request)).jsonObject

        assertFalse(json.containsKey("role"))
        assertEquals(
            listOf("pm", "backend", "frontend"),
            json.getValue("roles").jsonArray.map { it.jsonPrimitive.content },
        )
    }

    @Test
    fun editRequestWritesTheEntireUniqueSelectionUnderRoles() {
        val request = editModel(listOf("backend", "pm", "design", "backend")).toRequest()
        val json = Json.parseToJsonElement(Json.encodeToString(request)).jsonObject

        assertFalse(json.containsKey("role"))
        assertEquals(
            listOf("pm", "design", "backend"),
            json.getValue("roles").jsonArray.map { it.jsonPrimitive.content },
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun createRejectsEmptyRoles() {
        TaskCreateModel("태스크", emptyList(), "low").toDto()
    }

    @Test(expected = IllegalArgumentException::class)
    fun createRejectsUnsupportedRoles() {
        TaskCreateModel("태스크", listOf("pm", "qa"), "low").toDto()
    }

    @Test(expected = IllegalArgumentException::class)
    fun editRejectsEmptyRoles() {
        editModel(emptyList()).toRequest()
    }

    @Test(expected = IllegalArgumentException::class)
    fun editRejectsUnsupportedRoles() {
        editModel(listOf("backend", "qa")).toRequest()
    }

    private fun editModel(roles: List<String>): TaskEditModel = TaskEditModel(
        title = "태스크",
        roles = roles,
        assigneeId = null,
        priority = "low",
        status = "todo",
        purpose = "목적",
        checklistItems = emptyList(),
    )

    private fun detailJson(roles: String): String =
        """
        {
          "id": "1", "project_id": "project", "title": "태스크", "status": "todo", "roles": $roles,
          "assignee": null, "priority": "low", "purpose": null,
          "checklist": {"completed_count": 0, "total_count": 0, "items": []},
          "materials": [], "open_questions": [], "next_action": null
        }
        """.trimIndent()
}
