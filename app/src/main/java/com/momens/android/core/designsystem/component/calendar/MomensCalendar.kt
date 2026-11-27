package com.momens.android.core.designsystem.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.component.button.MomensToggleButton
import com.momens.android.core.designsystem.theme.MomensTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MomensCalendar(
    yearMonth: YearMonth,
    selectionMode: CalendarSelectionMode,
    selection: CalendarSelection,
    onSelectionChange: (CalendarSelection) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now(),
    disabledDates: Set<LocalDate> = emptySet(),
    originalDate: LocalDate? = null,
) {
    val weeks = remember(yearMonth) { calendarWeeks(yearMonth) }

    Column(modifier = modifier.background(MomensTheme.colors.white)) {
        CalendarMonthHeader(
            yearMonth = yearMonth,
            onMonthChange = onMonthChange,
        )

        Spacer(modifier = Modifier.height(4.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            thickness = 1.dp,
            color = MomensTheme.colors.gray200,
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
                            CalendarDateCell(
                                date = date,
                                state = dateState(date, selection, disabledDates, today, yearMonth, originalDate),
                                onClick = { onSelectionChange(selection.selectDate(date, selectionMode)) },
                            )
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
            .noRippleClickable(onClick = onClick),
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
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = color,
                style = MomensTheme.typography.captionM10,
                textAlign = TextAlign.Center,
            )
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
        CalendarDateState.SELECTED,
        CalendarDateState.ORIGINAL,
            -> RoundedCornerShape(10.dp)

        CalendarDateState.RANGE_START -> RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
        CalendarDateState.RANGE_END -> RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)

        CalendarDateState.TODAY,
        CalendarDateState.RANGE_MIDDLE,
        CalendarDateState.DEFAULT,
        CalendarDateState.PREVIOUS_MONTH,
        CalendarDateState.DISABLED,
            -> RectangleShape
    }

    val background = when (state) {
        CalendarDateState.SELECTED,
        CalendarDateState.RANGE_START,
        CalendarDateState.RANGE_END,
            -> MomensTheme.colors.primary100

        CalendarDateState.RANGE_MIDDLE -> MomensTheme.colors.primary10
        CalendarDateState.ORIGINAL -> MomensTheme.colors.gray200
        else -> Color.Transparent
    }

    val textColor = when (state) {
        CalendarDateState.SELECTED,
        CalendarDateState.ORIGINAL,
        CalendarDateState.RANGE_START,
        CalendarDateState.RANGE_END,
            -> MomensTheme.colors.white

        CalendarDateState.TODAY,
        CalendarDateState.RANGE_MIDDLE,
            -> MomensTheme.colors.primary100

        CalendarDateState.DISABLED -> MomensTheme.colors.black.copy(alpha = 0.4f)
        CalendarDateState.PREVIOUS_MONTH -> MomensTheme.colors.gray300
        CalendarDateState.DEFAULT -> MomensTheme.colors.black
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(background)
            .then(
                if (state == CalendarDateState.DISABLED) Modifier
                else Modifier.noRippleClickable(onClick = onClick),
            )
            .semantics {
                selected = state == CalendarDateState.SELECTED ||
                    state == CalendarDateState.RANGE_START ||
                    state == CalendarDateState.RANGE_MIDDLE ||
                    state == CalendarDateState.RANGE_END
                if (state == CalendarDateState.DISABLED) {
                    disabled()
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

@Preview(name = "MomensCalendar", showBackground = true)
@Composable
private fun MomensCalendarPreview() {
    var yearMonth by remember { mutableStateOf(YearMonth.of(2027, 1)) }
    var selectionMode by remember { mutableStateOf(CalendarSelectionMode.RANGE) }
    var selection by remember {
        mutableStateOf<CalendarSelection>(
            CalendarSelection.Range(
                start = LocalDate.of(2027, 1, 9),
                end = LocalDate.of(2027, 1, 11),
            ),
        )
    }

    MomensTheme {
        Column(
            modifier = Modifier
                .width(320.dp)
                .background(MomensTheme.colors.white),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalendarSelectionMode.entries.forEach { mode ->
                    MomensToggleButton(
                        text = when (mode) {
                            CalendarSelectionMode.SINGLE -> "단일 선택"
                            CalendarSelectionMode.RANGE -> "기간 선택"
                        },
                        isSelected = selectionMode == mode,
                        onCheckedChange = { isSelected ->
                            if (isSelected) {
                                selectionMode = mode
                                selection = CalendarSelection.None
                            }
                        },
                    )
                }
            }

            MomensCalendar(
                yearMonth = yearMonth,
                selectionMode = selectionMode,
                selection = selection,
                onSelectionChange = { selection = it },
                onMonthChange = { yearMonth = it },
                today = LocalDate.of(2027, 1, 15),
                disabledDates = setOf(LocalDate.of(2027, 1, 13)),
                originalDate = LocalDate.of(2027, 1, 12),
            )
        }
    }
}
