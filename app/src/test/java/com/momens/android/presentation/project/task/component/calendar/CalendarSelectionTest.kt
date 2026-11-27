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
}
