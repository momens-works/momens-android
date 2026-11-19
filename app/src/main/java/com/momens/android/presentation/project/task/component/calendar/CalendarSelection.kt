package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate

sealed interface CalendarSelection {
    data object None : CalendarSelection

    data class Single(val date: LocalDate) : CalendarSelection

    data class Range(val start: LocalDate, val end: LocalDate) : CalendarSelection {
        init {
            require(!end.isBefore(start)) { "Range end must not precede start" }
        }
    }
}
