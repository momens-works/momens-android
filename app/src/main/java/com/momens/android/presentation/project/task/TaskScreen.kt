package com.momens.android.presentation.project.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.momens.android.core.designsystem.component.type.MomensSnackbarType
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.core.designsystem.trigger.LocalGlobalUiEventTrigger
import com.momens.android.core.designsystem.trigger.SnackbarState
import com.momens.android.presentation.loading.LoadingScreen
import com.momens.android.presentation.project.task.component.TaskBottomSheet
import com.momens.android.presentation.project.task.component.TaskStatusBox
import com.momens.android.presentation.project.task.component.TaskTitle
import kotlinx.coroutines.delay

@Composable
fun TaskRoute(
    paddingValues: PaddingValues,
    navigateToTaskDetail: (String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val projectContext by viewModel.projectContext.collectAsStateWithLifecycle()
    val isCreatingTask by viewModel.isCreatingTask.collectAsStateWithLifecycle()
    val globalUiEvent = LocalGlobalUiEventTrigger.current

    val renderState = rememberMinDurationUiState(uiState)
    var pendingActionSnackbar by remember { mutableStateOf<SnackbarState?>(null) }
    var showTaskBottomSheet by remember { mutableStateOf(false) }
    var sheetProjectId by remember { mutableStateOf<String?>(null) }
    var sheetErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(projectContext.projectId) {
        viewModel.loadTaskBoard(projectContext.projectId)
        if (!isCreatingTask && sheetProjectId != projectContext.projectId) {
            showTaskBottomSheet = false
            sheetProjectId = null
            sheetErrorMessage = null
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TaskSideEffect.TaskCreated -> {
                    showTaskBottomSheet = false
                    sheetProjectId = null
                    sheetErrorMessage = null
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

                is TaskSideEffect.TaskCreationFailed -> {
                    if (showTaskBottomSheet && sheetProjectId == effect.projectId &&
                        viewModel.projectContext.value.projectId == effect.projectId
                    ) {
                        sheetErrorMessage = effect.message
                    } else {
                        showTaskBottomSheet = false
                        sheetProjectId = null
                        globalUiEvent.showSnackbar(
                            SnackbarState(content = MomensSnackbarModel(title = effect.message)),
                        )
                    }
                }

                is TaskSideEffect.NavigateToTaskDetail -> navigateToTaskDetail(effect.taskId)
            }
        }
    }

    LaunchedEffect(showTaskBottomSheet, pendingActionSnackbar) {
        val snackbar = pendingActionSnackbar ?: return@LaunchedEffect
        if (!showTaskBottomSheet) {
            globalUiEvent.showSnackbar(snackbar)
            pendingActionSnackbar = null
        }
    }

    LaunchedEffect(sheetErrorMessage) {
        if (sheetErrorMessage != null) {
            delay(2_000)
            sheetErrorMessage = null
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
                onAddClick = {
                    sheetProjectId = projectContext.projectId
                    sheetErrorMessage = null
                    showTaskBottomSheet = true
                },
            )

        UiState.Empty -> Unit
    }

    if (showTaskBottomSheet) {
        TaskBottomSheet(
            isCreatingTask = isCreatingTask,
            errorMessage = sheetErrorMessage,
            onDismiss = {
                showTaskBottomSheet = false
                sheetProjectId = null
                sheetErrorMessage = null
            },
            onRegisterClick = viewModel::addTask,
        )
    }
}

@Composable
private fun TaskScreen(
    paddingValues: PaddingValues,
    uiState: TaskUiState,
    avatarUrl: String?,
    onTaskClick: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp),
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
                onAddClick = {},
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
                onAddClick = {},
            )
        }
    }
}


