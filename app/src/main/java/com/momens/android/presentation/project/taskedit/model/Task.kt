package com.momens.android.presentation.project.taskedit.model

import androidx.compose.runtime.Immutable
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.data.project.taskedit.model.ChecklistItemsModel
import com.momens.android.data.project.taskedit.model.TaskEditModel
import com.momens.android.presentation.project.model.Assignee
import com.momens.android.presentation.project.model.TaskRole
import com.momens.android.presentation.project.taskdetail.model.TaskDetailModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

@Immutable
data class Task(
    val taskId: String,
    val titleState: String,
    val status: MomensStatusEditType,
    val role: TaskRole?,
    val assignee: Assignee?,
    val priority: ImportantLevel,
    val purposeState: String,
    val checklist: PersistentList<ChecklistItemState>,
    val originalRoles: ImmutableList<TaskRole> = listOfNotNull(role).toImmutableList(),
) {
    val checklistTotalCount: Int get() = checklist.size
    val checklistCompletedCount: Int get() = checklist.count { it.completed }

    companion object {
        val Fake = Task(
            taskId = "1",
            titleState = "로그인 화면 구현",
            status = MomensStatusEditType.TODO,
            role = TaskRole.BACKEND,
            assignee = Assignee("1", "김민지", null),
            priority = ImportantLevel.MEDIUM,
            purposeState = "사용자 인증 플로우 완성",
            checklist = persistentListOf(
                ChecklistItemState(id = "1", localId = "1", title = "로그인 API 연동 완료", completed = true),
                ChecklistItemState(id = "2", localId = "2", title = "에러 핸들링 처리", completed = false),
            ),
        )
    }
}

fun TaskDetailModel.toEditTask(): Task = Task(
    taskId = id,
    titleState = title,
    status = status,
    role = role,
    assignee = assignee?.let { Assignee(id = it.id, name = it.name, url = it.avatarUrl) },
    priority = priority,
    purposeState = purpose ?: "",
    checklist = checklist.items.map {
        ChecklistItemState(
            id = it.id,
            localId = UUID.randomUUID().toString(),
            title = it.title,
            completed = it.completed,
        )
    }.toPersistentList(),
    originalRoles = roles,
)

fun Task.toTaskEditModel(): TaskEditModel = TaskEditModel(
    title = titleState,
    // 역할을 변경하지 않은 저장에서는 서버의 복수 역할을 그대로 유지한다.
    roles = if (role == originalRoles.firstOrNull()) {
        originalRoles.map { it.name.lowercase() }
    } else {
        listOfNotNull(role?.name?.lowercase())
    },
    assigneeId = assignee?.id,
    priority = priority.label,
    status = status.key,
    purpose = purposeState,
    checklistItems = checklist
        .filter { it.title.isNotBlank() }
        .map { ChecklistItemsModel(id = it.id, title = it.title, completed = it.completed) },
)
