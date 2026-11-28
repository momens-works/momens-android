package com.momens.android.presentation.brief.model

import androidx.compose.runtime.Immutable
import com.momens.android.core.designsystem.component.type.MomensChipButtonType
import com.momens.android.core.model.signal.SignalFilterType
import com.momens.android.data.brief.model.BriefSignalSummaryFilterModel
import com.momens.android.presentation.brief.extension.toFilterType

@Immutable
data class BriefSignalSummaryFilter(
    val type: SignalFilterType,
    val label: String,
    val count: Int,
) {
    val chipButtonType: MomensChipButtonType
        get() = type.briefChipButtonType
}

private val SignalFilterType.briefChipButtonType: MomensChipButtonType
    get() = when (this) {
        SignalFilterType.ALL -> MomensChipButtonType.BLACK
        SignalFilterType.CHANGE -> MomensChipButtonType.YELLOW
        SignalFilterType.DECISION -> MomensChipButtonType.PURPLE
        SignalFilterType.QUESTION -> MomensChipButtonType.MINT
        SignalFilterType.RISK -> MomensChipButtonType.RED
    }

fun BriefSignalSummaryFilterModel.toUiModel(): BriefSignalSummaryFilter {
    val type = key.toFilterType()

    return BriefSignalSummaryFilter(
        type = type,
        label = label.ifBlank { type.name.lowercase().replaceFirstChar(Char::uppercaseChar) },
        count = count,
    )
}
