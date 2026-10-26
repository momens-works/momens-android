package com.momens.android.presentation.project.taskedit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.core.local.project.ProjectManager
import com.momens.android.data.project.taskdetail.repository.TaskDetailRepository
import com.momens.android.data.project.taskedit.repository.TaskEditRepository
import com.momens.android.presentation.project.model.Assignee
import com.momens.android.presentation.project.model.TaskRole
import com.momens.android.presentation.project.model.toAssignee
import com.momens.android.presentation.project.taskdetail.model.toUiModel
import com.momens.android.presentation.project.taskedit.model.ChecklistItemState
import com.momens.android.presentation.project.taskedit.model.toEditTask
import com.momens.android.presentation.project.taskedit.model.toTaskEditModel
import com.momens.android.presentation.project.taskedit.navigation.TaskEdit
import com.momens.android.presentation.project.taskedit.state.TaskEditSideEffect
import com.momens.android.presentation.project.taskedit.state.TaskEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    projectManager: ProjectManager,
    savedStateHandle: SavedStateHandle,
    private val taskEditRepository: TaskEditRepository,
    taskDetailRepository: TaskDetailRepository,
) : ViewModel() {

    val projectContext = projectManager.observeProjectContext().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = projectManager.currentProjectContext,
    )

    private val taskId: String = savedStateHandle.toRoute<TaskEdit>().taskId
    private val initialTask = taskDetailRepository.cachedTaskDetail.value!!.toUiModel().toEditTask()

    private val _state = MutableStateFlow(
        TaskEditState(
            task = initialTask,
            assignees = persistentListOf(),
        ),
    )
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<TaskEditSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun changeRole(role: TaskRole) {
        _state.update {
            it.copy(task = it.task.copy(role = role))
        }
    }

    fun changePriority(priority: ImportantLevel) {
        _state.update {
            it.copy(task = it.task.copy(priority = priority))
        }
    }

    fun changeStatus(status: MomensStatusEditType) {
        _state.update {
            it.copy(task = it.task.copy(status = status))
        }
    }

    fun addChecklistItem() {
        if (_state.value.task.checklistTotalCount >= 5) {
            viewModelScope.launch {
                _sideEffect.emit(
                    TaskEditSideEffect.ShowSnackBar(
                        message = "항목은 최대 5개까지 등록할 수 있습니다.",
                    ),
                )
            }
            return
        }

        _state.update {
            val newItem = ChecklistItemState(
                id = null,
                localId = UUID.randomUUID().toString(),
                title = "",
                completed = false,
            )
            it.copy(
                task = it.task.copy(
                    checklist = it.task.checklist.add(newItem),
                ),
            )
        }
    }

    fun clearChecklistItem(localId: String) {
        _state.update { state ->
            state.copy(
                task = state.task.copy(
                    checklist = state.task.checklist.removeAll { it.localId == localId },
                ),
            )
        }
    }

    fun updateChecklistTitle(localId: String, title: String) {
        _state.update { state ->
            state.copy(
                task = state.task.copy(
                    checklist = state.task.checklist.map {
                        if (it.localId == localId) it.copy(title = title) else it
                    }.toPersistentList(),
                ),
            )
        }
    }

    fun changeCheck(localId: String, checked: Boolean) {
        _state.update { state ->
            state.copy(
                task = state.task.copy(
                    checklist = state.task.checklist.map {
                        if (it.localId == localId) it.copy(completed = checked) else it
                    }.toPersistentList(),
                ),
            )
        }
    }

    fun reorderChecklist(fromIndex: Int, toIndex: Int) {
        _state.update { state ->
            val checklist = state.task.checklist
            if (fromIndex !in checklist.indices || toIndex !in checklist.indices) return@update state

            val reordered = checklist.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }.toPersistentList()

            state.copy(task = state.task.copy(checklist = reordered))
        }
    }

    fun updateAssignee(assignee: Assignee) {
        _state.update { it.copy(task = it.task.copy(assignee = assignee)) }
    }

    fun removeAssignee() {
        _state.update { it.copy(task = it.task.copy(assignee = null)) }
    }

    fun getAssignees(search: String) {
        viewModelScope.launch {
            val projectId = projectContext.value.projectId ?: run {
                _sideEffect.emit(TaskEditSideEffect.ShowSnackBar(message = "프로젝트 정보를 불러오지 못했습니다."))
                return@launch
            }

            taskEditRepository.getTaskEditMembers(
                projectId = projectId,
                query = search.ifBlank { null },
            ).onSuccess { response ->
                _state.update {
                    it.copy(
                        assignees = response.map { member ->
                            member.toAssignee()
                        }.toImmutableList(),
                    )
                }
            }.onFailure { throwable ->
                _sideEffect.emit(
                    TaskEditSideEffect.ShowSnackBar(message = throwable.message ?: "담당자 검색에 실패했습니다."),
                )
            }
        }
    }

    fun clearAssignees() {
        _state.update { it.copy(assignees = persistentListOf()) }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _sideEffect.emit(TaskEditSideEffect.NavigateUp)
        }
    }

    fun saveTask(title: String, purpose: String) {
        _state.update {
            it.copy(task = it.task.copy(titleState = title.ifBlank { "새 태스크" }, purposeState = purpose))
        }

        viewModelScope.launch {
            taskEditRepository.patchTaskEdit(
                taskId = taskId,
                request = _state.value.task.toTaskEditModel(),
            ).onSuccess {
                _sideEffect.emit(
                    TaskEditSideEffect.ShowSnackBar(
                        message = "저장되었습니다.",
                    ),
                )
                _sideEffect.emit(TaskEditSideEffect.NavigateUp)
            }.onFailure {
                _sideEffect.emit(
                    TaskEditSideEffect.ShowSnackBar(
                        message = "저장에 실패했습니다.",
                    ),
                )
            }
        }
    }
}
