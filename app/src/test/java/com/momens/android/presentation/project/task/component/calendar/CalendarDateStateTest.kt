package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarDateStateTest {
    private val today = LocalDate.of(2026, 10, 1)
    private val range = CalendarSelection.Range(
        start = LocalDate.of(2026, 9, 30),
        end = LocalDate.of(2026, 10, 2),
    )

    @Test
    fun `range state crosses month boundary and takes priority over today`() {
        assertEquals(
            CalendarDateState.RANGE_START,
            dateState(range.start, range, emptySet(), today),
        )
        assertEquals(
            CalendarDateState.RANGE_MIDDLE,
            dateState(today, range, emptySet(), today),
        )
        assertEquals(
            CalendarDateState.RANGE_END,
            dateState(range.end, range, emptySet(), today),
        )
    }

    @Test
    fun `disabled date cannot show selected state`() {
        assertEquals(
            CalendarDateState.DISABLED,
            dateState(today, CalendarSelection.Single(today), setOf(today), today),
        )
    }

    @Test
    fun `single day range uses selected state`() {
        val selection = CalendarSelection.Range(today, today)

        assertEquals(
            CalendarDateState.SELECTED,
            dateState(today, selection, emptySet(), today),
        )
    }

    @Test
    fun `multiple selection highlights only chosen dates`() {
        val selectedDate = today.plusDays(2)
        val unselectedDate = today.plusDays(1)
        val selection = CalendarSelection.Multiple(setOf(today, selectedDate))

        assertEquals(CalendarDateState.SELECTED, dateState(selectedDate, selection, emptySet(), today))
        assertEquals(CalendarDateState.DEFAULT, dateState(unselectedDate, selection, emptySet(), today))
    }
}
