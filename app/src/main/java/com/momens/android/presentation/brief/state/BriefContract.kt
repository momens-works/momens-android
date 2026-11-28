package com.momens.android.presentation.brief.state

import androidx.compose.runtime.Immutable
import com.momens.android.data.brief.model.BriefModel
import com.momens.android.data.brief.model.BriefPriorityModel
import com.momens.android.presentation.brief.model.BriefPriorityUiModel
import com.momens.android.presentation.brief.model.BriefProjectUiModel
import com.momens.android.core.model.signal.SignalFilterType
import com.momens.android.presentation.brief.model.BriefSignalSummaryUiModel
import com.momens.android.presentation.brief.model.toUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class BriefUiState(
    val project: BriefProjectUiModel,
    val signalSummary: BriefSignalSummaryUiModel,
    val priorities: ImmutableList<BriefPriorityUiModel>,
)

fun BriefModel.toUiState(
    selectedFilterType: SignalFilterType = SignalFilterType.ALL,
): BriefUiState = BriefUiState(
    project = project.toUiModel(),
    signalSummary = signalSummary.toUiModel(selectedFilterType = selectedFilterType),
    priorities = priorities.map(BriefPriorityModel::toUiModel).toImmutableList(),
)
