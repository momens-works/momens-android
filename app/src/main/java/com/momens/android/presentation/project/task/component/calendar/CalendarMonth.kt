package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate
import java.time.YearMonth

/** 일요일부터 토요일까지 한 줄씩, 달 밖의 칸은 null로 채운 월간 그리드를 만든다. */
fun calendarWeeks(yearMonth: YearMonth): List<List<LocalDate?>> {
    // DayOfWeek는 월요일이 1이므로 일요일 시작 달력에 맞춰 0부터 다시 센다.
    val leadingEmptyDays = yearMonth.atDay(1).dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()
    val totalDays = leadingEmptyDays + daysInMonth
    // 마지막 주도 7칸이 되도록 전체 칸 수를 올림한다.
    val totalCells = ((totalDays + 6) / 7) * 7

    return (0 until totalCells).chunked(7).map { week ->
        week.map { cell ->
            val day = cell - leadingEmptyDays + 1
            if (day in 1..daysInMonth) yearMonth.atDay(day) else null
        }
    }
}
