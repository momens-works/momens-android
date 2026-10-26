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

    fun loadTaskBoard(projectId: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            taskRepository.getTaskBoard(projectId)
                .mapCatching { board -> board.toUiModel() }
                .onSuccess { content ->
                    _uiState.value = UiState.Success(content)
                }
                .onLogFailure("Failed to load TaskBoard") {
                    _uiState.value = UiState.Failure
                    _sideEffect.emit(TaskSideEffect.ShowSnackbar("태스크를 불러오지 못했습니다"))
                }
        }
    }

    fun addTask(
        title: String,
        role: MomensTaskButtonType,
        priority: ImportantLevel,
    ) {
        val projectId = projectContext.value.projectId ?: return

        viewModelScope.launch {
            val previousBoard = (_uiState.value as? UiState.Success)?.data
            _uiState.value = UiState.Loading

            taskRepository.createTask(
                projectId = projectId,
                task = TaskCreateModel(
                    title = title,
                    role = role.toRequestValue(),
                    priority = priority.toRequestValue(),
                ),
            ).mapCatching { created -> created.toUiModel() }
                .onSuccess { newTask ->
                    val base = previousBoard ?: TaskUiState()
                    _uiState.value = UiState.Success(
                        base.copy(
                            sections = base.sections.map { section ->
                                if (section.type == MomensStatusEditType.TODO) {
                                    section.copy(tasks = (listOf(newTask) + section.tasks).toPersistentList())
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
                    _uiState.value = previousBoard
                        ?.let { UiState.Success(it) }
                        ?: UiState.Failure
                    _sideEffect.emit(TaskSideEffect.ShowSnackbar("태스크 등록에 실패했습니다"))
                }
        }
    }
}
