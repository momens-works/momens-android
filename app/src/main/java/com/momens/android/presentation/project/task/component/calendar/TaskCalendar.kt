package com.momens.android.presentation.project.task.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.designsystem.theme.MomensTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun TaskCalendar(
    yearMonth: YearMonth,
    selection: CalendarSelection,
    onDateClick: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now(),
    disabledDates: Set<LocalDate> = emptySet(),
) {
    val weeks = remember(yearMonth) { calendarWeeks(yearMonth) }

    Column(modifier = modifier.background(MomensTheme.colors.white)) {
        CalendarMonthHeader(
            yearMonth = yearMonth,
            onMonthChange = onMonthChange,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(1.dp)
                .background(MomensTheme.colors.gray200),
        )

        Spacer(modifier = Modifier.height(3.dp))

        CalendarWeekdayHeader()

        Spacer(modifier = Modifier.height(4.dp))

        Column(modifier = Modifier.padding(horizontal = 13.dp)) {
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                        ) {
                            if (date != null) {
                                CalendarDateCell(
                                    date = date,
                                    state = dateState(date, selection, disabledDates, today),
                                    onClick = { onDateClick(date) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarMonthHeader(
    yearMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MonthArrow(
            isPrevious = true,
            onClick = { onMonthChange(yearMonth.minusMonths(1)) },
        )
        Text(
            text = "${yearMonth.year}년 ${yearMonth.monthValue}월",
            modifier = Modifier.weight(1f),
            color = MomensTheme.colors.gray900,
            style = MomensTheme.typography.bodyB14,
            textAlign = TextAlign.Center,
        )
        MonthArrow(
            isPrevious = false,
            onClick = { onMonthChange(yearMonth.plusMonths(1)) },
        )
    }
}

@Composable
private fun MonthArrow(
    isPrevious: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(R.drawable.ic_next),
        contentDescription = null,
        modifier = modifier
            .size(24.dp)
            .then(if (isPrevious) Modifier.graphicsLayer(scaleX = -1f) else Modifier)
            .clickable(onClick = onClick),
        tint = MomensTheme.colors.black,
    )
}

@Composable
private fun CalendarWeekdayHeader(modifier: Modifier = Modifier) {
    val weekdays = listOf("일", "월", "화", "수", "목", "금", "토")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 13.dp, vertical = 8.dp),
    ) {
        weekdays.forEachIndexed { index, label ->
            val color = when (index) {
                0 -> MomensTheme.colors.pointRed
                6 -> MomensTheme.colors.primary50
                else -> MomensTheme.colors.black
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    color = color,
                    style = MomensTheme.typography.captionM10,
                )
            }
        }
    }
}

@Composable
private fun CalendarDateCell(
    date: LocalDate,
    state: CalendarDateState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = when (state) {
        CalendarDateState.TODAY,
        CalendarDateState.SELECTED,
            -> RoundedCornerShape(10.dp)

        CalendarDateState.RANGE_START -> RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
        CalendarDateState.RANGE_END -> RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
        CalendarDateState.RANGE_MIDDLE -> RoundedCornerShape(0.dp)
        else -> RoundedCornerShape(80.dp)
    }
    val background = when (state) {
        CalendarDateState.SELECTED,
        CalendarDateState.RANGE_START,
        CalendarDateState.RANGE_END,
            -> MomensTheme.colors.primary100

        CalendarDateState.RANGE_MIDDLE -> MomensTheme.colors.primary10
        else -> Color.Transparent
    }
    val textColor = when (state) {
        CalendarDateState.SELECTED,
        CalendarDateState.RANGE_START,
        CalendarDateState.RANGE_END,
            -> MomensTheme.colors.white

        CalendarDateState.TODAY,
        CalendarDateState.RANGE_MIDDLE,
            -> MomensTheme.colors.primary100

        CalendarDateState.DISABLED -> MomensTheme.colors.black.copy(alpha = 0.4f)
        CalendarDateState.DEFAULT -> MomensTheme.colors.black
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(background)
            .then(
                if (state == CalendarDateState.DISABLED) Modifier else Modifier.clickable(onClick = onClick),
            )
            .semantics {
                selected = state == CalendarDateState.SELECTED ||
                    state == CalendarDateState.RANGE_START ||
                    state == CalendarDateState.RANGE_MIDDLE ||
                    state == CalendarDateState.RANGE_END
                when (state) {
                    CalendarDateState.RANGE_START -> stateDescription = "범위 시작"
                    CalendarDateState.RANGE_MIDDLE -> stateDescription = "범위 중간"
                    CalendarDateState.RANGE_END -> stateDescription = "범위 끝"
                    CalendarDateState.DISABLED -> {
                        disabled()
                        stateDescription = "선택할 수 없음"
                    }

                    else -> Unit
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            style = MomensTheme.typography.bodyM14,
        )
    }
}

@Preview(name = "Default", showBackground = true)
@Composable
private fun TaskCalendarDefaultPreview() {
    val today = remember { LocalDate.now() }

    MomensTheme {
        TaskCalendar(
            yearMonth = YearMonth.from(today),
            selection = CalendarSelection.Single(today),
            today = today,
            onDateClick = {},
            onMonthChange = {},
            modifier = Modifier.width(320.dp),
        )
    }
}

@Preview(name = "Selected", showBackground = true)
@Composable
private fun TaskCalendarSelectedPreview() {
    MomensTheme {
        TaskCalendar(
            yearMonth = YearMonth.of(2026, 10),
            selection = CalendarSelection.Single(LocalDate.of(2026, 10, 9)),
            onDateClick = {},
            onMonthChange = {},
            modifier = Modifier.width(320.dp),
            today = LocalDate.of(2026, 10, 1),
        )
    }
}

@Preview(name = "Range", showBackground = true)
@Composable
private fun TaskCalendarRangePreview() {
    MomensTheme {
        TaskCalendar(
            yearMonth = YearMonth.of(2026, 10),
            selection = CalendarSelection.Range(
                start = LocalDate.of(2026, 10, 9),
                end = LocalDate.of(2026, 10, 13),
            ),
            onDateClick = {},
            onMonthChange = {},
            modifier = Modifier.width(320.dp),
            today = LocalDate.of(2026, 10, 1),
        )
    }
}

@Preview(name = "All states", showBackground = true, widthDp = 320, heightDp = 350)
@Composable
private fun TaskCalendarAllStatesPreview() {
    val today = remember { LocalDate.now() }
    var yearMonth by remember { mutableStateOf(YearMonth.from(today)) }
    var selection by remember { mutableStateOf<CalendarSelection>(CalendarSelection.Single(today)) }
    var hasUserSelectedDate by remember { mutableStateOf(false) }

    MomensTheme {
        TaskCalendar(
            yearMonth = yearMonth,
            selection = selection,
            onDateClick = { date ->
                selection = if (!hasUserSelectedDate) {
                    CalendarSelection.Single(date)
                } else {
                    when (val current = selection) {
                        CalendarSelection.None -> CalendarSelection.Single(date)
                        is CalendarSelection.Single -> {
                            if (current.date == date) {
                                CalendarSelection.None
                            } else {
                                CalendarSelection.Range(
                                    start = minOf(current.date, date),
                                    end = maxOf(current.date, date),
                                )
                            }
                        }

                        is CalendarSelection.Range -> CalendarSelection.Single(date)
                    }
                }
                hasUserSelectedDate = true
            },
            onMonthChange = { yearMonth = it },
            modifier = Modifier.width(320.dp),
            today = today,
        )
    }
}
