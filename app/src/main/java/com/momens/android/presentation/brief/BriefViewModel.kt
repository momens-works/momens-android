package com.momens.android.presentation.brief

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momens.android.core.common.extension.updateSuccess
import com.momens.android.core.common.state.UiState
import com.momens.android.core.local.project.ProjectManager
import com.momens.android.data.brief.repository.BriefRepository
import com.momens.android.presentation.brief.model.BriefSignalSummaryFilterType
import com.momens.android.presentation.brief.model.toApiFilter
import com.momens.android.presentation.brief.model.toUiModel
import com.momens.android.presentation.brief.state.BriefUiState
import com.momens.android.presentation.brief.state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BriefViewModel @Inject constructor(
    projectManager: ProjectManager,
    private val briefRepository: BriefRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<BriefUiState>>(UiState.Loading)
    private var signalFilterJob: Job? = null
    private var isLoadingMoreSignalSummary = false

    val uiState: StateFlow<UiState<BriefUiState>> = _uiState.asStateFlow()

    val projectContext = projectManager.observeProjectContext().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = projectManager.currentProjectContext,
    )

    fun loadBrief(projectId: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            briefRepository.getBrief(projectId = projectId)
                .onSuccess { response ->
                    Timber.tag(BRIEF_LOG_TAG).i(
                        "getBrief 응답 수신: projectId=$projectId, response=$response",
                    )
                    _uiState.value = UiState.Success(response.toUiState())
                }
                .onFailure { throwable ->
                    Timber.tag(BRIEF_LOG_TAG).e(
                        throwable,
                        "getBrief 요청 실패: projectId=$projectId",
                    )
                    _uiState.value = UiState.Failure
                }
        }
    }

    fun selectSignalFilter(filterType: BriefSignalSummaryFilterType) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return
        val filter = filterType.toApiFilter()
        val requestedProjectId = currentState.project.id

        signalFilterJob?.cancel()
        _uiState.updateSuccess { state ->
            state.copy(
                signalSummary = state.signalSummary.copy(
                    selectedFilterType = filterType,
                    items = persistentListOf(),
                    nextCursor = null,
                    isExpanded = false,
                ),
            )
        }

        signalFilterJob = viewModelScope.launch {
            briefRepository.getSignalSummary(
                projectId = requestedProjectId,
                filter = filter,
                cursor = null,
                limit = null,
            ).onSuccess { response ->
                Timber.tag(BRIEF_LOG_TAG).i(
                    "getSignalSummary 응답 수신: projectId=$requestedProjectId, " +
                        "filter=$filter, cursor=null, response=$response",
                )
                _uiState.updateSuccess { state ->
                    if (
                        state.project.id != requestedProjectId ||
                        state.signalSummary.selectedFilterType != filterType
                    ) {
                        return@updateSuccess state
                    }

                    state.copy(
                        signalSummary = state.signalSummary.copy(
                            selectedFilterType = filterType,
                            items = response.items.map { item -> item.toUiModel() }.toImmutableList(),
                            nextCursor = response.nextCursor,
                            isExpanded = false,
                        ),
                    )
                }
            }.onFailure { throwable ->
                Timber.tag(BRIEF_LOG_TAG).e(
                    throwable,
                    "getSignalSummary 요청 실패: projectId=$requestedProjectId, " +
                        "filter=$filter, cursor=null",
                )
            }
        }
    }

    fun loadMoreSignalSummary() {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return
        val signalSummary = currentState.signalSummary

        if (!signalSummary.isExpanded) {
            updateSignalSummaryExpanded(isExpanded = true)
            return
        }

        val nextCursor = signalSummary.nextCursor

        if (nextCursor == null || isLoadingMoreSignalSummary) return

        val filter = signalSummary.selectedFilterType.toApiFilter()
        val requestedProjectId = currentState.project.id

        Timber.tag(BRIEF_LOG_TAG).i(
            "getSignalSummary 더보기 요청 시작: projectId=$requestedProjectId, " +
                "filter=$filter, cursor=$nextCursor",
        )

        isLoadingMoreSignalSummary = true

        viewModelScope.launch {
            briefRepository.getSignalSummary(
                projectId = requestedProjectId,
                filter = filter,
                cursor = nextCursor,
                limit = null,
            ).onSuccess { response ->
                Timber.tag(BRIEF_LOG_TAG).i(
                    "getSignalSummary 더보기 응답 수신: projectId=$requestedProjectId, " +
                        "filter=$filter, cursor=$nextCursor, response=$response",
                )
                _uiState.updateSuccess { state ->
                    val currentSummary = state.signalSummary
                    if (
                        state.project.id != requestedProjectId ||
                        currentSummary.selectedFilterType != signalSummary.selectedFilterType ||
                        currentSummary.nextCursor != nextCursor
                    ) {
                        return@updateSuccess state
                    }

                    state.copy(
                        signalSummary = currentSummary.copy(
                            items = (
                                currentSummary.items +
                                    response.items.map { item -> item.toUiModel() }
                                ).toImmutableList(),
                            nextCursor = response.nextCursor,
                            isExpanded = true,
                        ),
                    )
                }
            }.onFailure { throwable ->
                Timber.tag(BRIEF_LOG_TAG).e(
                    throwable,
                    "getSignalSummary 더보기 요청 실패: projectId=$requestedProjectId, " +
                        "filter=$filter, cursor=$nextCursor",
                )
            }.also {
                isLoadingMoreSignalSummary = false
            }
        }
    }

    fun foldSignalSummary() {
        updateSignalSummaryExpanded(isExpanded = false)
    }

    private fun updateSignalSummaryExpanded(isExpanded: Boolean) {
        _uiState.updateSuccess { currentState ->
            currentState.copy(
                signalSummary = currentState.signalSummary.copy(isExpanded = isExpanded),
            )
        }
    }

    companion object {
        private const val BRIEF_LOG_TAG = "BriefApi"
    }
}
