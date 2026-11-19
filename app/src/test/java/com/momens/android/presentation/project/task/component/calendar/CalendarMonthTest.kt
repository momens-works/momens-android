package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** 월 시작 요일과 4·5·6주 달의 빈 칸 배치를 확인한다. */
class CalendarMonthTest {
    @Test
    fun `month starts on correct weekday and ends with empty cells`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 10))

        assertEquals(5, weeks.size)
        assertEquals(7, weeks.first().size)
        assertNull(weeks.first()[0])
        assertEquals(LocalDate.of(2026, 10, 1), weeks.first()[4])
        assertEquals(LocalDate.of(2026, 10, 31), weeks.last()[6])
    }

    @Test
    fun `month spanning six weeks keeps all dates`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 8))

        assertEquals(6, weeks.size)
        assertEquals(LocalDate.of(2026, 8, 1), weeks.first()[6])
        assertEquals(LocalDate.of(2026, 8, 31), weeks.last()[1])
        assertNull(weeks.last()[6])
    }

    @Test
    fun `february beginning on sunday needs only four weeks`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 2))

        assertEquals(4, weeks.size)
        assertEquals(LocalDate.of(2026, 2, 1), weeks.first().first())
        assertEquals(LocalDate.of(2026, 2, 28), weeks.last().last())
    }

    @Test
    fun `thirty day month leaves remaining cells empty`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 11))

        assertEquals(LocalDate.of(2026, 11, 30), weeks.last()[1])
        assertNull(weeks.last()[2])
    }
}
