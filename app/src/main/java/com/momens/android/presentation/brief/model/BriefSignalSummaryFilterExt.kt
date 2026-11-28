package com.momens.android.presentation.brief.model

import com.momens.android.core.model.signal.SignalFilterType

fun SignalFilterType.toApiFilter(): String? {
    return if (this == SignalFilterType.ALL) {
        null
    } else {
        name.lowercase()
    }
}
