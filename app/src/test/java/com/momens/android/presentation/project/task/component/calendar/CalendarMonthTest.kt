package com.momens.android.presentation.project.task.component.calendar

import com.momens.android.core.designsystem.component.calendar.calendarWeeks
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarMonthTest {
    @Test
    fun `month starts on correct weekday with previous month dates`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 10))

        assertEquals(5, weeks.size)
        assertEquals(7, weeks.first().size)
        assertEquals(LocalDate.of(2026, 9, 27), weeks.first()[0])
        assertEquals(LocalDate.of(2026, 10, 1), weeks.first()[4])
        assertEquals(LocalDate.of(2026, 10, 31), weeks.last()[6])
    }

    @Test
    fun `month spanning six weeks keeps all dates`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 8))

        assertEquals(6, weeks.size)
        assertEquals(LocalDate.of(2026, 8, 1), weeks.first()[6])
        assertEquals(LocalDate.of(2026, 8, 31), weeks.last()[1])
        assertEquals(LocalDate.of(2026, 9, 5), weeks.last()[6])
    }

    @Test
    fun `february beginning on sunday needs only four weeks`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 2))

        assertEquals(4, weeks.size)
        assertEquals(LocalDate.of(2026, 2, 1), weeks.first().first())
        assertEquals(LocalDate.of(2026, 2, 28), weeks.last().last())
    }

    @Test
    fun `thirty day month fills remaining cells with next month dates`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 11))

        assertEquals(LocalDate.of(2026, 11, 30), weeks.last()[1])
        assertEquals(LocalDate.of(2026, 12, 1), weeks.last()[2])
        assertEquals(LocalDate.of(2026, 12, 5), weeks.last()[6])
    }

    @Test
    fun `january includes previous year dates`() {
        val dates = calendarWeeks(YearMonth.of(2027, 1)).flatten()

        assertEquals(LocalDate.of(2026, 12, 27), dates.first())
        assertEquals(LocalDate.of(2027, 2, 6), dates.last())
        assertTrue(dates.zipWithNext().all { (first, second) -> second == first.plusDays(1) })
    }

    @Test
    fun `december includes next year dates`() {
        val weeks = calendarWeeks(YearMonth.of(2026, 12))

        assertEquals(LocalDate.of(2026, 11, 29), weeks.first().first())
        assertEquals(LocalDate.of(2027, 1, 2), weeks.last().last())
    }

    @Test
    fun `leap february includes leap day and adjacent month dates`() {
        val weeks = calendarWeeks(YearMonth.of(2028, 2))
        val dates = weeks.flatten()

        assertEquals(5, weeks.size)
        assertEquals(LocalDate.of(2028, 1, 30), dates.first())
        assertTrue(LocalDate.of(2028, 2, 29) in dates)
        assertEquals(LocalDate.of(2028, 3, 4), dates.last())
    }
}
