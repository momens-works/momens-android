package com.momens.android.presentation.signal.model

import com.momens.android.core.model.signal.SignalFilterType

val SignalFilterType.label: String
    get() = when (this) {
        SignalFilterType.ALL -> "All"
        SignalFilterType.CHANGE -> "Change"
        SignalFilterType.DECISION -> "Decision"
        SignalFilterType.QUESTION -> "Question"
        SignalFilterType.RISK -> "Risk"
    }
