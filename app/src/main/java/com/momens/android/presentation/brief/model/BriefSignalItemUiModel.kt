package com.momens.android.presentation.brief.model

import androidx.compose.runtime.Immutable
import com.momens.android.core.model.signal.SignalFilterType
import com.momens.android.data.brief.model.BriefSignalSummaryItemModel
import com.momens.android.presentation.brief.extension.toFilterType

@Immutable
data class BriefSignalItemUiModel(
    val id: String,
    val type: SignalFilterType,
    val title: String,
)

fun BriefSignalSummaryItemModel.toUiModel(): BriefSignalItemUiModel = BriefSignalItemUiModel(
    id = id,
    type = type.toFilterType(),
    title = title,
)
