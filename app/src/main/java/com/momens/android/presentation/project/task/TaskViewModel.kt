package com.momens.android.presentation.project.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momens.android.core.common.extension.onLogFailure
import com.momens.android.core.common.state.UiState
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.data.project.task.model.TaskCreateModel
import com.momens.android.data.project.task.repository.TaskRepository
import com.momens.android.core.local.project.ProjectManager
import com.momens.android.presentation.project.task.model.MomensTaskButtonType
import com.momens.android.presentation.project.task.model.toRequestValue
import com.momens.android.presentation.project.task.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel @Inject constructor(
    projectManager: ProjectManager,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<TaskUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<TaskUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<TaskSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val projectContext = projectManager.observeProjectContext().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = projectManager.currentProjectContext,
    )

    private var boardProjectId: String? = null
    private var boardJob: Job? = null
    private val _isCreatingTask = MutableStateFlow(false)
    val isCreatingTask: StateFlow<Boolean> = _isCreatingTask.asStateFlow()

    fun loadTaskBoard(projectId: String?) {
        boardJob?.cancel()
        if (projectId.isNullOrBlank()) {
            boardProjectId = null
            _uiState.value = UiState.Empty
            return
        }

        boardProjectId = projectId
        _uiState.value = UiState.Loading

        boardJob = viewModelScope.launch {
            taskRepository.getTaskBoard(projectId)
                .mapCatching { it.toUiModel() }
                .onSuccess { board ->
                    if (boardProjectId == projectId) {
                        _uiState.value = UiState.Success(board)
                    }
                }
                .onLogFailure("Failed to load TaskBoard") {
                    if (boardProjectId == projectId) {
                        _uiState.value = UiState.Failure
                        _sideEffect.emit(TaskSideEffect.ShowSnackbar("태스크를 불러오지 못했습니다"))
                    }
                }
        }
    }

    fun addTask(
        title: String,
        role: MomensTaskButtonType,
        priority: ImportantLevel,
    ) {
        val projectId = projectContext.value.projectId ?: return
        if (projectId != boardProjectId || _isCreatingTask.value) return
        if (_uiState.value !is UiState.Success) return

        _isCreatingTask.value = true

        viewModelScope.launch {
            try {
                taskRepository.createTask(
                    projectId = projectId,
                    task = TaskCreateModel(
                        title = title,
                        role = role.toRequestValue(),
                        priority = priority.toRequestValue(),
                    ),
                )
                    .mapCatching { it.toUiModel() }
                    .onSuccess { newTask ->
                        if (boardProjectId != projectId) return@onSuccess

                        val currentBoard = (_uiState.value as? UiState.Success)?.data
                            ?: return@onSuccess

                        _uiState.value = UiState.Success(
                            currentBoard.copy(
                                sections = currentBoard.sections.map { section ->
                                    if (section.type == MomensStatusEditType.TODO) {
                                        section.copy(
                                            tasks = (listOf(newTask) + section.tasks).toPersistentList(),
                                        )
                                    } else {
                                        section
                                    }
                                }.toPersistentList(),
                            ),
                        )
                        _sideEffect.emit(
                            TaskSideEffect.ShowActionSnackbar(
                                message = "태스크가 등록되었습니다",
                                description = "'투두'에 추가됨",
                                taskId = newTask.id,
                            ),
                        )
                    }
                    .onLogFailure("Failed to create task") {
                        if (boardProjectId == projectId) {
                            _sideEffect.emit(TaskSideEffect.ShowSnackbar("태스크 등록에 실패했습니다"))
                        }
                    }
            } finally {
                _isCreatingTask.value = false
            }
        }
    }
}
