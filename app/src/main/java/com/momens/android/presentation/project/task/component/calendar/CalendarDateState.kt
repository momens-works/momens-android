package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate

enum class CalendarDateState {
    DEFAULT,
    TODAY,
    SELECTED,
    DISABLED,
    RANGE_START,
    RANGE_MIDDLE,
    RANGE_END,
}

/** 비활성 날짜를 먼저 처리한 뒤 선택 범위, 오늘, 기본 상태 순서로 셀 모양을 결정한다. */
fun dateState(
    date: LocalDate,
    selection: CalendarSelection,
    disabledDates: Set<LocalDate>,
    today: LocalDate,
): CalendarDateState {
    if (date in disabledDates) return CalendarDateState.DISABLED

    when (selection) {
        CalendarSelection.None -> Unit
        is CalendarSelection.Single -> if (date == selection.date) return CalendarDateState.SELECTED
        is CalendarSelection.Range -> {
            if (selection.start == selection.end && date == selection.start) return CalendarDateState.SELECTED
            if (date == selection.start) return CalendarDateState.RANGE_START
            if (date == selection.end) return CalendarDateState.RANGE_END
            if (date.isAfter(selection.start) && date.isBefore(selection.end)) {
                return CalendarDateState.RANGE_MIDDLE
            }
        }
    }

    return if (date == today) CalendarDateState.TODAY else CalendarDateState.DEFAULT
}
