package com.momens.android.presentation.project.task.component.calendar

import java.time.LocalDate

/** 태스크 생성 화면이 캘린더에 전달하는 선택 결과. 선택 진행 중에는 Single을 사용한다. */
sealed interface CalendarSelection {
    data object None : CalendarSelection

    data class Single(val date: LocalDate) : CalendarSelection

    /** 시작일과 종료일이 모두 정해진 기간 선택. */
    data class Range(val start: LocalDate, val end: LocalDate) : CalendarSelection {
        init {
            require(!end.isBefore(start)) { "Range end must not precede start" }
        }
    }
}
