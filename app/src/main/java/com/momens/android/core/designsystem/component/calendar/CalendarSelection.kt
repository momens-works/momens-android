package com.momens.android.core.designsystem.component.calendar

import java.time.LocalDate

enum class CalendarSelectionMode {
    SINGLE,
}

sealed interface CalendarSelection {
    data object None : CalendarSelection

    data class Single(val date: LocalDate) : CalendarSelection

    data class Range(val start: LocalDate, val end: LocalDate) : CalendarSelection {
        init {
            require(!end.isBefore(start)) { "Range end must not precede start" }
        }
    }
}

fun CalendarSelection.selectDate(
    date: LocalDate,
    mode: CalendarSelectionMode,
): CalendarSelection = when (mode) {
    CalendarSelectionMode.SINGLE -> CalendarSelection.Single(date)
}
