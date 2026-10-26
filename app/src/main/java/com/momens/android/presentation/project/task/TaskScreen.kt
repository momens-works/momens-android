package com.momens.android.presentation.project.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.core.common.extension.rememberMinDurationUiState
import com.momens.android.core.common.state.UiState
import com.momens.android.core.designsystem.component.button.MomensFloatingActionButton
import com.momens.android.core.designsystem.component.header.MomensDefaultHeader
import com.momens.android.core.designsystem.component.snackbar.model.MomensSnackbarModel
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensSnackbarType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.core.designsystem.trigger.LocalGlobalUiEventTrigger
import com.momens.android.core.designsystem.trigger.SnackbarState
import com.momens.android.presentation.loading.LoadingScreen
import com.momens.android.presentation.project.task.component.TaskBottomSheet
import com.momens.android.presentation.project.task.component.TaskStatusBox
import com.momens.android.presentation.project.task.component.TaskTitle
import com.momens.android.presentation.project.task.model.MomensTaskButtonType

@Composable
fun TaskRoute(
    paddingValues: PaddingValues,
    navigateToTaskDetail: (String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val projectContext by viewModel.projectContext.collectAsStateWithLifecycle()
    val globalUiEvent = LocalGlobalUiEventTrigger.current

    val renderState = rememberMinDurationUiState(uiState)
    var pendingActionSnackbar by remember { mutableStateOf<SnackbarState?>(null) }

    LaunchedEffect(projectContext.projectId) {
        projectContext.projectId?.let(viewModel::loadTaskBoard)
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TaskSideEffect.ShowActionSnackbar -> {
                    pendingActionSnackbar = SnackbarState(
                        content = MomensSnackbarModel(
                            title = effect.message,
                            description = effect.description,
                            type = MomensSnackbarType.BUTTON,
                            onActionClick = { navigateToTaskDetail(effect.taskId) },
                        ),
                    )
                }

                is TaskSideEffect.ShowSnackbar -> {
                    globalUiEvent.showSnackbar(
                        SnackbarState(content = MomensSnackbarModel(title = effect.message)),
                    )
                }

                is TaskSideEffect.NavigateToTaskDetail -> navigateToTaskDetail(effect.taskId)
            }
        }
    }

    LaunchedEffect(renderState, pendingActionSnackbar) {
        val snackbar = pendingActionSnackbar ?: return@LaunchedEffect
        if (renderState is UiState.Success) {
            globalUiEvent.showSnackbar(snackbar)
            pendingActionSnackbar = null
        }
    }

    when (renderState) {
        UiState.Loading, UiState.Failure -> {
            LoadingScreen()
        }

        is UiState.Success ->
            TaskScreen(
                paddingValues = paddingValues,
                onTaskClick = navigateToTaskDetail,
                uiState = renderState.data,
                avatarUrl = projectContext.avatarUrl,
                onRegisterClick = viewModel::addTask,
            )

        UiState.Empty -> Unit
    }
}

@Composable
private fun TaskScreen(
    paddingValues: PaddingValues,
    uiState: TaskUiState,
    avatarUrl: String?,
    onTaskClick: (String) -> Unit,
    onRegisterClick: (title: String, role: MomensTaskButtonType, priority: ImportantLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showTaskBottomSheet by remember { mutableStateOf(false) }
    val titleState = rememberTextFieldState()
    var selectedRole by remember { mutableStateOf<MomensTaskButtonType?>(null) }
    var selectedPriority by remember { mutableStateOf<ImportantLevel?>(null) }

    val resetTaskInput = {
        titleState.clearText()
        selectedRole = null
        selectedPriority = null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MomensTheme.colors.uiBg)
            .padding(paddingValues),
    ) {
        Column {
            MomensDefaultHeader(
                onProfileClick = {},
                avatarUrl = avatarUrl,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            TaskTitle(
                title = uiState.title,
                description = uiState.description,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 50.dp),
            ) {
                items(
                    items = uiState.sections,
                    key = { it.type },
                ) { section ->
                    TaskStatusBox(
                        type = section.type,
                        tasks = section.tasks,
                        onTaskClick = { task -> onTaskClick(task.id) },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp),
                    )
                }
            }
        }

        MomensFloatingActionButton(
            onClick = { showTaskBottomSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp),
        )
    }

    if (showTaskBottomSheet) {
        TaskBottomSheet(
            titleState = titleState,
            selectedRole = selectedRole,
            selectedPriority = selectedPriority,
            onRoleSelect = { selectedRole = it },
            onPrioritySelect = { selectedPriority = it },
            onDismiss = {
                resetTaskInput()
                showTaskBottomSheet = false
            },
            onRegisterClick = {
                onRegisterClick(
                    titleState.text.toString(),
                    requireNotNull(selectedRole),
                    requireNotNull(selectedPriority),
                )
                resetTaskInput()
                showTaskBottomSheet = false
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskScreenPreview() {
    MomensTheme {
        Scaffold { innerPadding ->
            TaskScreen(
                paddingValues = innerPadding,
                uiState = TaskUiState.Fake,
                avatarUrl = null,
                onTaskClick = {},
                onRegisterClick = { _, _, _ -> },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskScreenEmptyPreview() {
    MomensTheme {
        Scaffold { innerPadding ->
            TaskScreen(
                paddingValues = innerPadding,
                uiState = TaskUiState.FakeEmpty,
                avatarUrl = null,
                onTaskClick = {},
                onRegisterClick = { _, _, _ -> },
            )
        }
    }
}


