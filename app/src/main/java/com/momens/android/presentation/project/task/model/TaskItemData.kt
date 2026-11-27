package com.momens.android.presentation.project.task.model

import androidx.compose.runtime.Immutable
import com.momens.android.core.common.extension.limitLength
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.ImportantTone
import com.momens.android.data.project.task.model.TaskItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class TaskItemData(
    val id: String,
    val title: String,
    val role: MomensTaskButtonType?,
    val priority: ImportantLevel,
    val materialCount: Int,
    val tone: ImportantTone,
    // 복수 역할 UI 적용 전까지 단일 역할 화면과 서버의 원본 목록을 함께 유지한다.
    val roles: ImmutableList<MomensTaskButtonType> = listOfNotNull(role).toImmutableList(),
)

private const val TITLE_MAX_LENGTH = 15

fun TaskItem.toUiModel(): TaskItemData = TaskItemData(
    id = id,
    title = title.limitLength(TITLE_MAX_LENGTH),
    role = roles.firstOrNull()?.toButtonType(),
    priority = priority.toImportantLevel(),
    materialCount = materialCount,
    tone = ImportantTone.WHITE,
    roles = roles.map { it.toButtonType() }.toImmutableList(),
)

fun MomensTaskButtonType.toRequestValue(): String = name.lowercase()

fun ImportantLevel.toRequestValue(): String = name.lowercase()

private fun String.toButtonType(): MomensTaskButtonType =
    MomensTaskButtonType.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: MomensTaskButtonType.PM

private fun String.toImportantLevel(): ImportantLevel =
    ImportantLevel.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: ImportantLevel.LOW
