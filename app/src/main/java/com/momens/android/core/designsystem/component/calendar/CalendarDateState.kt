package com.momens.android.core.designsystem.component.calendar

import java.time.LocalDate

enum class CalendarDateState {
    DEFAULT,
    TODAY,
    SELECTED,
    ORIGINAL,
    DISABLED,
    RANGE_START,
    RANGE_MIDDLE,
    RANGE_END,
}

fun dateState(
    date: LocalDate,
    selection: CalendarSelection,
    disabledDates: Set<LocalDate>,
    today: LocalDate,
    originalDate: LocalDate? = null,
): CalendarDateState {
    if (date in disabledDates) return CalendarDateState.DISABLED

    when (selection) {
        CalendarSelection.None -> Unit
        is CalendarSelection.Single -> if (date == selection.date) return CalendarDateState.SELECTED
        is CalendarSelection.Multiple -> if (date in selection.dates) return CalendarDateState.SELECTED
        is CalendarSelection.Range -> {
            if (selection.start == selection.end && date == selection.start) return CalendarDateState.SELECTED
            if (date == selection.start) return CalendarDateState.RANGE_START
            if (date == selection.end) return CalendarDateState.RANGE_END
            if (date.isAfter(selection.start) && date.isBefore(selection.end)) {
                return CalendarDateState.RANGE_MIDDLE
            }
        }
    }

    if (date == originalDate) return CalendarDateState.ORIGINAL

    return if (date == today) CalendarDateState.TODAY else CalendarDateState.DEFAULT
}
