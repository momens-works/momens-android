package com.momens.android.core.designsystem.component.calendar

import java.time.LocalDate
import java.time.YearMonth

fun calendarWeeks(yearMonth: YearMonth): List<List<LocalDate?>> {
    val leadingEmptyDays = yearMonth.atDay(1).dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()
    val totalDays = leadingEmptyDays + daysInMonth
    val totalCells = ((totalDays + 6) / 7) * 7

    return (0 until totalCells).chunked(7).map { week ->
        week.map { cell ->
            val day = cell - leadingEmptyDays + 1
            if (day in 1..daysInMonth) yearMonth.atDay(day) else null
        }
    }
}
