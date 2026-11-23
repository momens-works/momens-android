package com.momens.android.presentation.project.task.component.calendar

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
    fun `multiple mode toggles nonconsecutive dates independently`() {
        val firstSelection = CalendarSelection.None.selectDate(first, CalendarSelectionMode.MULTIPLE)
        val secondSelection = firstSelection.selectDate(second, CalendarSelectionMode.MULTIPLE)

        assertEquals(CalendarSelection.Multiple(setOf(first, second)), secondSelection)
        assertEquals(
            CalendarSelection.Multiple(setOf(second)),
            secondSelection.selectDate(first, CalendarSelectionMode.MULTIPLE),
        )
        assertEquals(
            CalendarSelection.None,
            secondSelection.selectDate(first, CalendarSelectionMode.MULTIPLE)
                .selectDate(second, CalendarSelectionMode.MULTIPLE),
        )
    }

    @Test
    fun `multiple mode starts a new selection after a displayed range`() {
        val range = CalendarSelection.Range(first, first.plusDays(2))

        assertEquals(
            CalendarSelection.Multiple(setOf(second)),
            range.selectDate(second, CalendarSelectionMode.MULTIPLE),
        )
    }
}
