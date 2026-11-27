package com.momens.android.core.designsystem.component.calendar

import java.time.LocalDate
import java.time.YearMonth

fun calendarWeeks(yearMonth: YearMonth): List<List<LocalDate>> {
    val firstDay = yearMonth.atDay(1)
    val leadingDays = firstDay.dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()
    val totalDays = leadingDays + daysInMonth
    val totalCells = ((totalDays + 6) / 7) * 7
    val gridStartDate = firstDay.minusDays(leadingDays.toLong())

    return (0 until totalCells).chunked(7).map { week ->
        week.map { cell ->
            gridStartDate.plusDays(cell.toLong())
        }
    }
}
