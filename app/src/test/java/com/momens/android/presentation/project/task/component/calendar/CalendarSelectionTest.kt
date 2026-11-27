package com.momens.android.presentation.project.task.component.calendar

import com.momens.android.core.designsystem.component.calendar.CalendarSelection
import com.momens.android.core.designsystem.component.calendar.CalendarSelectionMode
import com.momens.android.core.designsystem.component.calendar.selectDate
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarSelectionTest {
    private val first = LocalDate.of(2026, 10, 9)
    private val second = LocalDate.of(2026, 10, 13)

    @Test
    fun `single mode replaces the previously selected date`() {
        val selection = CalendarSelection.Single(first)

        assertEquals(
            CalendarSelection.Single(second),
            selection.selectDate(second, CalendarSelectionMode.SINGLE),
        )
        assertEquals(
            CalendarSelection.Single(first),
            selection.selectDate(first, CalendarSelectionMode.SINGLE),
        )
    }

    @Test
    fun `range mode first click starts a pending selection`() {
        assertEquals(
            CalendarSelection.Single(first),
            CalendarSelection.None.selectDate(first, CalendarSelectionMode.RANGE),
        )
    }

    @Test
    fun `range mode second click completes the range`() {
        val pending = CalendarSelection.None.selectDate(first, CalendarSelectionMode.RANGE)

        assertEquals(
            CalendarSelection.Range(first, second),
            pending.selectDate(second, CalendarSelectionMode.RANGE),
        )
    }

    @Test
    fun `range mode sorts dates when the second click is earlier`() {
        val pending = CalendarSelection.None.selectDate(second, CalendarSelectionMode.RANGE)

        assertEquals(
            CalendarSelection.Range(first, second),
            pending.selectDate(first, CalendarSelectionMode.RANGE),
        )
    }

    @Test
    fun `range mode clicking the same date completes a single day range`() {
        val pending = CalendarSelection.None.selectDate(first, CalendarSelectionMode.RANGE)

        assertEquals(
            CalendarSelection.Range(first, first),
            pending.selectDate(first, CalendarSelectionMode.RANGE),
        )
    }

    @Test
    fun `range mode starts a new selection after a completed range`() {
        val range = CalendarSelection.Range(first, second)
        val nextStart = second.plusDays(2)
        val pending = range.selectDate(nextStart, CalendarSelectionMode.RANGE)

        assertEquals(CalendarSelection.Single(nextStart), pending)
        assertEquals(
            CalendarSelection.Range(nextStart, nextStart.plusDays(3)),
            pending.selectDate(nextStart.plusDays(3), CalendarSelectionMode.RANGE),
        )
    }

    @Test
    fun `range mode supports dates across a year boundary`() {
        val start = LocalDate.of(2026, 12, 31)
        val end = LocalDate.of(2027, 1, 2)
        val pending = CalendarSelection.None.selectDate(start, CalendarSelectionMode.RANGE)

        assertEquals(
            CalendarSelection.Range(start, end),
            pending.selectDate(end, CalendarSelectionMode.RANGE),
        )
    }

    @Test
    fun `single mode replaces a completed range with one date`() {
        assertEquals(
            CalendarSelection.Single(second),
            CalendarSelection.Range(first, second).selectDate(second, CalendarSelectionMode.SINGLE),
        )
    }
}
