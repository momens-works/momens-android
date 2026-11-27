package com.momens.android.presentation.project.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.core.common.extension.collectSideEffect
import com.momens.android.core.common.extension.rememberMinDurationUiState
import com.momens.android.core.common.state.UiState
import com.momens.android.core.designsystem.component.header.MomensHeader
import com.momens.android.core.designsystem.component.snackbar.model.MomensSnackbarModel
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.core.designsystem.trigger.LocalGlobalUiEventTrigger
import com.momens.android.core.designsystem.trigger.SnackbarState
import com.momens.android.presentation.loading.LoadingScreen
import com.momens.android.presentation.project.taskdetail.component.TaskDetailCompletionSection
import com.momens.android.presentation.project.taskdetail.component.TaskDetailFileBottomSheet
import com.momens.android.presentation.project.taskdetail.component.TaskDetailFileSection
import com.momens.android.presentation.project.taskdetail.component.TaskDetailInfoSection
import com.momens.android.presentation.project.taskdetail.component.TaskDetailNextActionSection
import com.momens.android.presentation.project.taskdetail.component.TaskDetailPurposeSection
import com.momens.android.presentation.project.taskdetail.component.TaskDetailQuestionSection
import com.momens.android.presentation.project.taskdetail.component.TaskDetailTitleSection
import com.momens.android.presentation.project.taskdetail.model.TaskDetailFileModel
import com.momens.android.presentation.project.taskdetail.model.TaskDetailModel
import com.momens.android.presentation.project.taskdetail.state.TaskDetailSideEffect
import com.momens.android.presentation.project.taskdetail.state.TaskDetailState
import com.momens.android.presentation.project.taskdetail.viewmodel.TaskDetailViewModel
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun TaskDetailRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToTaskEdit: (TaskDetailModel) -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current

    LifecycleResumeEffect(Unit) {
        viewModel.loadTaskDetail()
        onPauseOrDispose { }
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is TaskDetailSideEffect.ShowSnackbar -> {
                globalTrigger.showSnackbar(
                    SnackbarState(content = MomensSnackbarModel(title = it.message)),
                )
            }
        }
    }

    when (val renderState = rememberMinDurationUiState(state)) {
        UiState.Loading, UiState.Failure -> {
            LoadingScreen()
        }

        is UiState.Success -> {
            TaskDetailScreen(
                state = renderState.data,
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                onEditClick = { renderState.data.taskDetail?.let(navigateToTaskEdit) },
                onCheck = viewModel::toggleChecklistItem,
            )
        }

        UiState.Empty -> Unit
    }
}

@Composable
private fun TaskDetailScreen(
    state: TaskDetailState,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    onEditClick: () -> Unit,
    onCheck: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedFile by remember { mutableStateOf<TaskDetailFileModel?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MomensTheme.colors.white)
            .padding(paddingValues),
    ) {
        MomensHeader(
            text = "태스크 상세",
            onBackClick = navigateUp,
            isWriteVisible = true,
            onWriteClick = onEditClick,
        )

        state.taskDetail?.let { taskDetail ->
            TaskDetailContent(
                taskDetail = taskDetail,
                mutatingChecklistItemIds = state.mutatingChecklistItemIds,
                onCheck = onCheck,
                onFileClick = { selectedFile = it },
            )
        }
    }

    selectedFile?.let { file ->
        TaskDetailFileBottomSheet(
            file = file,
            onDismiss = { selectedFile = null },
            onOpenSourceClick = { },
        )
    }
}

@Composable
private fun TaskDetailContent(
    taskDetail: TaskDetailModel,
    mutatingChecklistItemIds: ImmutableSet<String>,
    onCheck: (String, Boolean) -> Unit,
    onFileClick: (TaskDetailFileModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                TaskDetailTitleSection(
                    taskDetailTitle = taskDetail.title,
                    status = taskDetail.status,
                )

                Spacer(modifier = Modifier.height(11.dp))

                TaskDetailInfoSection(
                    role = taskDetail.role?.label ?: "미지정",
                    assigneeName = taskDetail.assignee?.name ?: "미지정",
                    priority = taskDetail.priority.text,
                )
            }
        }

        item {
            TaskDetailPurposeSection(
                purpose = taskDetail.purpose,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            )
        }

        item {
            TaskDetailCompletionSection(
                completedCount = taskDetail.checklist.completedCount,
                totalCount = taskDetail.checklist.totalCount,
                items = taskDetail.checklist.items,
                mutatingItemIds = mutatingChecklistItemIds,
                onCheckedChange = onCheck,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            )
        }

        item {
            TaskDetailFileSection(
                files = taskDetail.materials,
                onFileClick = onFileClick,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            )
        }

        item {
            HorizontalDivider(
                color = MomensTheme.colors.gray100,
                thickness = 4.dp,
            )
        }

        item {
            TaskDetailQuestionSection(
                questions = taskDetail.openQuestions,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            )
        }


        item {
            TaskDetailNextActionSection(
                recommendationText = taskDetail.nextAction,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 0.dp)
                    .padding(bottom = 42.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskDetailScreenPreview() {
    MomensTheme {
        TaskDetailScreen(
            state = TaskDetailState.Fake,
            paddingValues = PaddingValues(),
            navigateUp = {},
            onEditClick = {},
            onCheck = { _, _ -> },
        )
    }
}

@Preview(showBackground = true, name = "로딩 / 데이터 없음")
@Composable
private fun TaskDetailScreenEmptyPreview() {
    MomensTheme {
        TaskDetailScreen(
            state = TaskDetailState(),
            paddingValues = PaddingValues(),
            navigateUp = {},
            onEditClick = {},
            onCheck = { _, _ -> },
        )
    }
}

@Preview(showBackground = true, name = "태스크 상세 - 아무것도 입력 안된 상태")
@Composable
private fun TaskDetailScreenEmptyFieldsPreview() {
    MomensTheme {
        TaskDetailScreen(
            state = TaskDetailState.FakeEmptyFields,
            paddingValues = PaddingValues(),
            navigateUp = {},
            onEditClick = {},
            onCheck = { _, _ -> },
        )
    }
}
