package com.momens.android.presentation.project.task.component.calendar

import com.momens.android.core.designsystem.component.calendar.CalendarDateState
import com.momens.android.core.designsystem.component.calendar.CalendarSelection
import com.momens.android.core.designsystem.component.calendar.dateState
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarDateStateTest {
    private val today = LocalDate.of(2026, 10, 1)
    private val yearMonth = YearMonth.from(today)
    private val range = CalendarSelection.Range(
        start = LocalDate.of(2026, 9, 30),
        end = LocalDate.of(2026, 10, 2),
    )

    @Test
    fun `original date stays visible alongside a new selection`() {
        val originalDate = today.plusDays(2)
        val selectedDate = today.plusDays(5)
        val selection = CalendarSelection.Single(selectedDate)

        assertEquals(
            CalendarDateState.ORIGINAL,
            dateState(originalDate, selection, emptySet(), today, yearMonth, originalDate),
        )
        assertEquals(
            CalendarDateState.SELECTED,
            dateState(selectedDate, selection, emptySet(), today, yearMonth, originalDate),
        )
    }

    @Test
    fun `selecting the original date uses the new selection style`() {
        assertEquals(
            CalendarDateState.SELECTED,
            dateState(
                today,
                CalendarSelection.Single(today),
                emptySet(),
                today,
                yearMonth,
                originalDate = today,
            ),
        )
    }

    @Test
    fun `original date takes priority over today without being selected`() {
        assertEquals(
            CalendarDateState.ORIGINAL,
            dateState(today, CalendarSelection.None, emptySet(), today, yearMonth, originalDate = today),
        )
    }

    @Test
    fun `disabled original date remains disabled`() {
        assertEquals(
            CalendarDateState.DISABLED,
            dateState(today, CalendarSelection.None, setOf(today), today, yearMonth, originalDate = today),
        )
    }

    @Test
    fun `range state crosses month boundary and takes priority over today`() {
        assertEquals(
            CalendarDateState.RANGE_START,
            dateState(range.start, range, emptySet(), today, yearMonth),
        )
        assertEquals(
            CalendarDateState.RANGE_MIDDLE,
            dateState(today, range, emptySet(), today, yearMonth),
        )
        assertEquals(
            CalendarDateState.RANGE_END,
            dateState(range.end, range, emptySet(), today, yearMonth),
        )
    }

    @Test
    fun `disabled date cannot show selected state`() {
        assertEquals(
            CalendarDateState.DISABLED,
            dateState(today, CalendarSelection.Single(today), setOf(today), today, yearMonth),
        )
    }

    @Test
    fun `single day range uses selected state`() {
        val selection = CalendarSelection.Range(today, today)

        assertEquals(
            CalendarDateState.SELECTED,
            dateState(today, selection, emptySet(), today, yearMonth),
        )
    }

    @Test
    fun `unselected previous month date uses previous month state`() {
        assertEquals(
            CalendarDateState.PREVIOUS_MONTH,
            dateState(range.start, CalendarSelection.None, emptySet(), today, yearMonth),
        )
    }

    @Test
    fun `unselected next month date uses default state`() {
        assertEquals(
            CalendarDateState.DEFAULT,
            dateState(LocalDate.of(2026, 11, 1), CalendarSelection.None, emptySet(), today, yearMonth),
        )
    }

    @Test
    fun `previous month state crosses year boundary`() {
        assertEquals(
            CalendarDateState.PREVIOUS_MONTH,
            dateState(
                LocalDate.of(2026, 12, 31),
                CalendarSelection.None,
                emptySet(),
                today,
                YearMonth.of(2027, 1),
            ),
        )
    }

    @Test
    fun `next year date uses default state`() {
        assertEquals(
            CalendarDateState.DEFAULT,
            dateState(
                LocalDate.of(2027, 1, 1),
                CalendarSelection.None,
                emptySet(),
                today,
                YearMonth.of(2026, 12),
            ),
        )
    }

    @Test
    fun `disabled previous month date remains disabled`() {
        assertEquals(
            CalendarDateState.DISABLED,
            dateState(range.start, CalendarSelection.None, setOf(range.start), today, yearMonth),
        )
    }
}
