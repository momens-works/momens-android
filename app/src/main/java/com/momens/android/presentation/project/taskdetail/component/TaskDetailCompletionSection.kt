package com.momens.android.presentation.project.taskdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.designsystem.component.checkbox.MomensCheckBox
import com.momens.android.core.designsystem.component.sectiontitle.MomensSectionTitle
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.project.model.ChecklistItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun TaskDetailCompletionSection(
    completedCount: Int,
    totalCount: Int,
    items: ImmutableList<ChecklistItem>,
    mutatingItemIds: ImmutableSet<String>,
    onCheckedChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MomensSectionTitle(
            title = "완료",
            count = "${completedCount}/${totalCount}",
            isEmphasized = true,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MomensTheme.colors.white,
                    shape = shape,
                )
                .border(
                    width = 1.dp,
                    color = MomensTheme.colors.gray100,
                    shape = shape,
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (items.isEmpty()) {
                Text(
                    text = "완료기준이 입력되지 않았습니다.",
                    style = MomensTheme.typography.bodyM12,
                    color = MomensTheme.colors.gray300,
                )
            } else {
                items.forEach { item ->
                    key(item.id) {
                        MomensCheckBox(
                            isChecked = item.completed,
                            label = item.title,
                            isToggleEnabled = item.id !in mutatingItemIds,
                            onCheckedChange = { checked -> onCheckedChange(item.id, checked) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskDetailCompletionSectionPreview() {
    MomensTheme {
        var items by remember {
            mutableStateOf(
                persistentListOf(
                    ChecklistItem(
                        id = "1",
                        title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반",
                        completed = true,
                    ),
                    ChecklistItem(
                        id = "2",
                        title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반",
                        completed = true,
                    ),
                    ChecklistItem(
                        id = "3",
                        title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반",
                        completed = false,
                    ),
                    ChecklistItem(
                        id = "4",
                        title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반",
                        completed = false,
                    ),
                ),
            )
        }

        TaskDetailCompletionSection(
            modifier = Modifier.padding(16.dp),
            completedCount = items.count { it.completed },
            totalCount = items.size,
            items = items,
            mutatingItemIds = persistentSetOf(),
            onCheckedChange = { id, checked ->
                items = items
                    .map { if (it.id == id) it.copy(completed = checked) else it }
                    .toPersistentList()
            },
        )
    }
}

@Preview(showBackground = true, name = "빈 상태")
@Composable
private fun TaskDetailCompletionSectionEmptyPreview() {
    MomensTheme {
        TaskDetailCompletionSection(
            modifier = Modifier.padding(16.dp),
            completedCount = 0,
            totalCount = 0,
            items = persistentListOf(),
            mutatingItemIds = persistentSetOf(),
            onCheckedChange = { _, _ -> },
        )
    }
}
