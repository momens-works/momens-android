package com.momens.android.presentation.brief.model

import androidx.compose.runtime.Immutable
import com.momens.android.core.model.signal.SignalFilterType
import com.momens.android.data.brief.model.BriefSignalSummaryFilterModel
import com.momens.android.data.brief.model.BriefSignalSummaryItemModel
import com.momens.android.data.brief.model.BriefSignalSummaryModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class BriefSignalSummaryUiModel(
    val summary: String,
    val filters: ImmutableList<BriefSignalSummaryFilter>,
    val selectedFilterType: SignalFilterType,
    val items: ImmutableList<BriefSignalItemUiModel>,
    val nextCursor: String?,
    val isExpanded: Boolean,
) {
    val totalCount: Int
        get() = filters.firstOrNull { it.type == SignalFilterType.ALL }?.count ?: 0

    val hasMore: Boolean
        get() = nextCursor != null || items.size > DEFAULT_VISIBLE_COUNT

    val hasNextPage: Boolean
        get() = nextCursor != null
}

private const val DEFAULT_VISIBLE_COUNT = 3

fun BriefSignalSummaryModel.toUiModel(
    selectedFilterType: SignalFilterType,
): BriefSignalSummaryUiModel = BriefSignalSummaryUiModel(
    summary = summary.orEmpty(),
    filters = filters.map(BriefSignalSummaryFilterModel::toUiModel).toImmutableList(),
    selectedFilterType = selectedFilterType,
    items = items.map(BriefSignalSummaryItemModel::toUiModel).toImmutableList(),
    nextCursor = nextCursor,
    isExpanded = false,
)
