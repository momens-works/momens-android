package com.momens.android.presentation.brief.extension

import com.momens.android.core.model.signal.SignalFilterType

fun String.toFilterType(): SignalFilterType {
    return SignalFilterType.entries.firstOrNull { filterType ->
        filterType.name.equals(this, ignoreCase = true)
    } ?: SignalFilterType.ALL
}
