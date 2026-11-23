package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate

enum class CalendarSelectionMode {
    SINGLE,
    MULTIPLE,
}

sealed interface CalendarSelection {
    data object None : CalendarSelection

    data class Single(val date: LocalDate) : CalendarSelection

    data class Multiple(val dates: Set<LocalDate>) : CalendarSelection

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
    CalendarSelectionMode.MULTIPLE -> {
        val selectedDates = when (this) {
            CalendarSelection.None -> emptySet()
            is CalendarSelection.Single -> setOf(this.date)
            is CalendarSelection.Multiple -> dates
            is CalendarSelection.Range -> emptySet()
        }
        val nextDates = if (date in selectedDates) selectedDates - date else selectedDates + date
        if (nextDates.isEmpty()) CalendarSelection.None else CalendarSelection.Multiple(nextDates)
    }
}
