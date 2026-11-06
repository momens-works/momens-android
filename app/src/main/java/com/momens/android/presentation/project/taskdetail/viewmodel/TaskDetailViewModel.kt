package com.momens.android.presentation.project.taskdetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.momens.android.core.common.extension.updateSuccess
import com.momens.android.core.common.state.UiState
import com.momens.android.core.common.util.successData
import com.momens.android.data.project.taskdetail.repository.TaskDetailRepository
import com.momens.android.presentation.project.model.toUiModel
import com.momens.android.presentation.project.taskdetail.model.toUiModel
import com.momens.android.presentation.project.taskdetail.navigation.TaskDetail
import com.momens.android.presentation.project.taskdetail.state.TaskDetailSideEffect
import com.momens.android.presentation.project.taskdetail.state.TaskDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskDetailRepository: TaskDetailRepository,
) : ViewModel() {
    val taskId: String = savedStateHandle.toRoute<TaskDetail>().taskId

    private val _state = MutableStateFlow<UiState<TaskDetailState>>(UiState.Loading)
    val state: StateFlow<UiState<TaskDetailState>> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<TaskDetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun loadTaskDetail() {
        viewModelScope.launch {
            taskDetailRepository.getTaskDetail(taskId = taskId)
                .onSuccess { detail ->
                    _state.update { currentState ->
                        val currentData = currentState.successData ?: TaskDetailState()
                        UiState.Success(
                            currentData.copy(taskDetail = detail.toUiModel()),
                        )
                    }
                }
                .onFailure {
                    if (_state.value !is UiState.Success) {
                        _state.value = UiState.Failure
                    }
                    _sideEffect.emit(TaskDetailSideEffect.ShowSnackbar(message = "태스크를 불러오지 못했습니다."))
                }
        }
    }

    fun toggleChecklistItem(itemId: String, completed: Boolean) {
        val state = _state.value.successData ?: return
        if (itemId in state.mutatingChecklistItemIds) return
        val previousChecklist = state.taskDetail?.checklist ?: return

        _state.updateSuccess { current ->
            val optimisticItems = previousChecklist.items.map { item ->
                if (item.id == itemId) item.copy(completed = completed) else item
            }.toPersistentList()

            current.copy(
                taskDetail = current.taskDetail?.copy(
                    checklist = previousChecklist.copy(
                        completedCount = if (completed) previousChecklist.completedCount + 1
                        else previousChecklist.completedCount - 1,
                        items = optimisticItems,
                    ),
                ),
                mutatingChecklistItemIds = (current.mutatingChecklistItemIds + itemId).toPersistentSet(),
            )
        }

        viewModelScope.launch {
            taskDetailRepository.updateChecklistItem(
                taskId = taskId,
                itemId = itemId,
                completed = completed,
            )
                .onSuccess { result ->
                    _state.updateSuccess { current ->
                        val currentChecklist = current.taskDetail?.checklist ?: return@updateSuccess current

                        current.copy(
                            taskDetail = current.taskDetail?.copy(
                                checklist = currentChecklist.copy(
                                    completedCount = result.completedCount,
                                    totalCount = result.totalCount,
                                    items = currentChecklist.items
                                        .map { if (it.id == result.item.id) result.item.toUiModel() else it }
                                        .toPersistentList(),
                                ),
                            ),
                        )
                    }
                }
                .onFailure {
                    _state.updateSuccess { current ->
                        current.copy(taskDetail = current.taskDetail?.copy(checklist = previousChecklist))
                    }
                    _sideEffect.emit(TaskDetailSideEffect.ShowSnackbar(message = "완료 상태 변경에 실패했습니다."))
                }
            _state.updateSuccess { current ->
                current.copy(mutatingChecklistItemIds = (current.mutatingChecklistItemIds - itemId).toPersistentSet())
            }
        }
    }
}
