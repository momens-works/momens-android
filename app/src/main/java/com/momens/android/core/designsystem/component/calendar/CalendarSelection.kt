package com.momens.android.core.designsystem.component.calendar

import java.time.LocalDate

enum class CalendarSelectionMode {
    SINGLE,
    RANGE,
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
    CalendarSelectionMode.RANGE -> when (this) {
        CalendarSelection.None,
        is CalendarSelection.Range,
            -> CalendarSelection.Single(date)

        is CalendarSelection.Single -> CalendarSelection.Range(
            start = minOf(this.date, date),
            end = maxOf(this.date, date),
        )
    }
}
