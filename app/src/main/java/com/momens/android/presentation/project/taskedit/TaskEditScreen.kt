package com.momens.android.presentation.project.taskedit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.core.common.extension.addFocusCleaner
import com.momens.android.presentation.project.taskedit.component.rememberDragDropState
import com.momens.android.core.common.extension.advancedImePadding
import com.momens.android.core.designsystem.component.header.MomensHeader
import com.momens.android.core.designsystem.component.snackbar.model.MomensSnackbarModel
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.core.designsystem.trigger.LocalGlobalUiEventTrigger
import com.momens.android.core.designsystem.trigger.SnackbarState
import com.momens.android.presentation.project.model.Assignee
import com.momens.android.presentation.project.model.TaskRole
import com.momens.android.presentation.project.taskedit.component.TaskEditCompleteSectionHeader
import com.momens.android.presentation.project.taskedit.component.TaskEditOptionSection
import com.momens.android.presentation.project.taskedit.component.TaskEditPurposeSection
import com.momens.android.presentation.project.taskedit.component.TaskEditTitleSection
import com.momens.android.presentation.project.taskedit.component.taskEditChecklistItems
import com.momens.android.presentation.project.taskedit.component.assignee.TaskEditAssigneeBottomSheet
import com.momens.android.presentation.project.taskedit.component.status.TaskEditStatusBottomSheet
import com.momens.android.presentation.project.taskedit.state.TaskEditSideEffect
import com.momens.android.presentation.project.taskedit.state.TaskEditState
import com.momens.android.presentation.project.taskedit.viewmodel.TaskEditViewModel

// 체크리스트 앞에 오는 LazyColumn item 개수(Spacer 4개 + Title/Option/Purpose 섹션 + 체크리스트 헤더).
// DragDropState가 LazyListState의 전역 index로 동작하므로, 로컬 체크리스트 index와의 변환에 사용된다.
private const val CHECKLIST_INDEX_OFFSET = 8

@Composable
fun TaskEditRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: TaskEditViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEvent = LocalGlobalUiEventTrigger.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TaskEditSideEffect.ShowSnackBar -> {
                    globalUiEvent.showSnackbar(
                        SnackbarState(content = MomensSnackbarModel(title = effect.message)),
                    )
                }

                is TaskEditSideEffect.NavigateUp -> navigateUp()
            }
        }
    }

    TaskEditScreen(
        paddingValues = paddingValues,
        state = state,
        onStatusChange = viewModel::changeStatus,
        onRoleSelect = viewModel::changeRole,
        onPriorityChange = viewModel::changePriority,
        onAssigneeChange = viewModel::updateAssignee,
        onAssigneeSearchClick = viewModel::getAssignees,
        onChecklistTitleChange = viewModel::updateChecklistTitle,
        onChecklistAddClick = viewModel::addChecklistItem,
        onCheckedChange = viewModel::changeCheck,
        onChecklistClearClick = viewModel::clearChecklistItem,
        onChecklistReorder = viewModel::reorderChecklist,
        onSaveClick = viewModel::saveTask,
        onBackClick = viewModel::navigateUp,
        onAssigneeDeleteClick = viewModel::removeAssignee,
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun TaskEditScreen(
    state: TaskEditState,
    onStatusChange: (MomensStatusEditType) -> Unit,
    onRoleSelect: (TaskRole) -> Unit,
    onPriorityChange: (ImportantLevel) -> Unit,
    onAssigneeChange: (Assignee) -> Unit,
    onAssigneeSearchClick: (String) -> Unit,
    onChecklistAddClick: () -> Unit,
    onCheckedChange: (String, Boolean) -> Unit,
    onChecklistTitleChange: (String, String) -> Unit,
    onChecklistClearClick: (String) -> Unit,
    onChecklistReorder: (Int, Int) -> Unit,
    onSaveClick: (String, String) -> Unit,
    onAssigneeDeleteClick: () -> Unit,
    onBackClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val task = state.task

    val titleState = remember { TextFieldState(task.titleState) }
    val purposeState = remember { TextFieldState(task.purposeState) }

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        onSwap = { from, to -> onChecklistReorder(from - CHECKLIST_INDEX_OFFSET, to - CHECKLIST_INDEX_OFFSET) },
    )

    var isAssigneeOpen by remember { mutableStateOf(false) }
    var isAssigneeClicked by remember { mutableStateOf(false) }

    var isStatusOpen by remember { mutableStateOf(false) }
    var isStatusClicked by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isAssigneeClicked, isStatusClicked, isImeVisible) {
        if (isAssigneeClicked && !isImeVisible) {
            isAssigneeOpen = true
            isAssigneeClicked = false
        }

        if (isStatusClicked && !isImeVisible) {
            isStatusOpen = true
            isStatusClicked = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MomensTheme.colors.white)
            .padding(paddingValues)
            .addFocusCleaner(focusManager)
            .advancedImePadding(),
    ) {
        MomensHeader(
            text = state.pageTitle,
            isSaveVisible = true,
            onSaveClick = { onSaveClick(titleState.text.toString(), purposeState.text.toString()) },
            onBackClick = onBackClick,
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .background(color = MomensTheme.colors.white)
                .padding(horizontal = 20.dp),
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                TaskEditTitleSection(
                    titleState = titleState,
                    status = task.status,
                    maxLength = 15,
                    onStatusClick = {
                        isStatusClicked = true
                        focusManager.clearFocus()
                    },
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                TaskEditOptionSection(
                    selectedRole = task.role,
                    selectedPriority = task.priority,
                    assigneeName = task.assignee?.name ?: "미지정",
                    onRoleSelect = onRoleSelect,
                    onPrioritySelect = onPriorityChange,
                    onAssigneeClick = {
                        isAssigneeClicked = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier,
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                TaskEditPurposeSection(
                    state = purposeState,
                    placeholder = "목적을 입력해주세요.",
                    modifier = Modifier,
                    maxLength = 300,
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                TaskEditCompleteSectionHeader(
                    completedCount = task.checklistCompletedCount,
                    totalCount = task.checklistTotalCount,
                    isEmpty = task.checklist.isEmpty(),
                    onAddClick = onChecklistAddClick,
                )
            }

            taskEditChecklistItems(
                rules = task.checklist,
                maxLength = 50,
                dragDropState = dragDropState,
                indexOffset = CHECKLIST_INDEX_OFFSET,
                onTitleChange = onChecklistTitleChange,
                onCheckedChange = onCheckedChange,
                onClearClick = onChecklistClearClick,
            )

            item { Spacer(modifier = Modifier.height(6.dp)) }
        }

    }


    if (isStatusOpen) {
        TaskEditStatusBottomSheet(
            status = task.status,
            onStatusChange = {
                onStatusChange(it)
                isStatusOpen = false
            },
            onDismiss = { isStatusOpen = false },
        )
    }

    if (isAssigneeOpen) {
        val searchState = remember { TextFieldState() }

        TaskEditAssigneeBottomSheet(
            state = searchState,
            selectedAssignee = task.assignee,
            assignees = state.assignees,
            onDismiss = { isAssigneeOpen = false },
            onAssigneeChange = onAssigneeChange,
            onDeleteClick = onAssigneeDeleteClick,
            onSearchClick = onAssigneeSearchClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskEditScreenPreview() {
    MomensTheme {
        var state by remember { mutableStateOf(TaskEditState.Fake) }

        TaskEditScreen(
            paddingValues = PaddingValues(0.dp),
            state = state,
            onStatusChange = {},
            onRoleSelect = {},
            onPriorityChange = {},
            onAssigneeChange = {},
            onChecklistAddClick = {},
            onCheckedChange = { _, _ -> },
            onChecklistClearClick = {},
            onSaveClick = { _, _ -> },
            onAssigneeDeleteClick = {},
            onAssigneeSearchClick = {},
            onBackClick = {},
            onChecklistTitleChange = { _, _ -> },
            onChecklistReorder = { _, _ -> },
        )
    }
}
