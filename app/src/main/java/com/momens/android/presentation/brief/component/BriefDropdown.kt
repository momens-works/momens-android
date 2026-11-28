package com.momens.android.presentation.brief.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.noRippleClickable
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.brief.model.BriefSignalItemUiModel
import com.momens.android.core.model.signal.SignalFilterType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

private const val MAX_VISIBLE_COUNT = 3
private val DROPDOWN_CONTENT_MAX_HEIGHT = 156.dp

@Composable
fun BriefDropdown(
    items: ImmutableList<BriefSignalItemUiModel>,
    hasMore: Boolean,
    canLoadMore: Boolean,
    expanded: Boolean,
    onMoreClick: () -> Unit,
    onLoadMore: () -> Unit,
    onFoldClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultItems = items.take(MAX_VISIBLE_COUNT)
    val expandableItems = items.drop(MAX_VISIBLE_COUNT)
    val showToggleButton = hasMore || expanded || expandableItems.isNotEmpty()
    val showMoreButton = !expanded
    val scrollState = rememberScrollState()

    LaunchedEffect(
        expanded,
        canLoadMore,
        scrollState,
    ) {
        if (!expanded || !canLoadMore) return@LaunchedEffect

        snapshotFlow {
            scrollState.maxValue > 0 && scrollState.value >= scrollState.maxValue
        }.distinctUntilChanged()
            .filter { isScrolledToEnd -> isScrolledToEnd }
            .collect {
                onLoadMore()
            }
    }

    val rotation by animateFloatAsState(
        targetValue = if (showMoreButton) 90f else 270f,
        label = "",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MomensTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp),
            )
            .background(
                color = MomensTheme.colors.white,
                shape = RoundedCornerShape(8.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = DROPDOWN_CONTENT_MAX_HEIGHT)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            defaultItems.forEachIndexed { index, item ->
                BriefDropdownRow(
                    item = item,
                )

                if (index != defaultItems.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            AnimatedVisibility(
                visible = expanded && expandableItems.isNotEmpty(),
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    expandableItems.forEach {
                        BriefDropdownRow(
                            item = it,
                        )
                    }
                }
            }
        }

        if (showToggleButton) {
            HorizontalDivider(
                color = MomensTheme.colors.gray100,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        if (showMoreButton) {
                            onMoreClick()
                        } else {
                            onFoldClick()
                        }
                    }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (showMoreButton) "더보기" else "접기",
                    style = MomensTheme.typography.captionB11,
                    color = MomensTheme.colors.gray400,
                )

                Spacer(
                    modifier = Modifier.width(2.dp),
                )

                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotation)
                        .size(16.dp),
                    tint = MomensTheme.colors.gray400,
                )
            }
        }
    }
}

@Composable
private fun BriefDropdownRow(
    item: BriefSignalItemUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = item.type.signalColor(),
                    shape = CircleShape,
                ),
        )

        Spacer(
            modifier = Modifier.width(8.dp),
        )

        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            color = MomensTheme.colors.gray800,
            style = MomensTheme.typography.captionB11,
        )
    }
}

@Composable
private fun SignalFilterType.signalColor(): Color = when (this) {
    SignalFilterType.CHANGE -> MomensTheme.colors.pointYellow
    SignalFilterType.DECISION -> MomensTheme.colors.pointPurple
    SignalFilterType.QUESTION -> MomensTheme.colors.pointMint
    SignalFilterType.RISK -> MomensTheme.colors.pointRed
    SignalFilterType.ALL -> MomensTheme.colors.gray400
}

@Preview(showBackground = true)
@Composable
private fun BriefDropdownPreview() {
    var twoItemExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    var manyItemExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    MomensTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            BriefDropdown(
                items = persistentListOf(
                    BriefSignalItemUiModel(
                        id = "6f3d8a61-4de7-4c01-9d2b-16fdf182e9a1",
                        type = SignalFilterType.DECISION,
                        title = "소셜 로그인은 MVP 범위에서 제외",
                    ),
                    BriefSignalItemUiModel(
                        id = "27afd507-9c7f-4f0d-a2be-fcdab2477b19",
                        type = SignalFilterType.DECISION,
                        title = "회원가입 MVP 범위 1차 확정",
                    ),
                ),
                hasMore = false,
                canLoadMore = false,
                expanded = twoItemExpanded,
                onMoreClick = {
                    twoItemExpanded = true
                },
                onLoadMore = {},
                onFoldClick = {
                    twoItemExpanded = false
                },
            )

            BriefDropdown(
                items = persistentListOf(
                    BriefSignalItemUiModel(
                        id = "6f3d8a61-4de7-4c01-9d2b-16fdf182e9a1",
                        type = SignalFilterType.DECISION,
                        title = "소셜 로그인은 MVP 범위에서 제외",
                    ),
                    BriefSignalItemUiModel(
                        id = "27afd507-9c7f-4f0d-a2be-fcdab2477b19",
                        type = SignalFilterType.DECISION,
                        title = "회원가입 MVP 범위 1차 확정",
                    ),
                    BriefSignalItemUiModel(
                        id = "3b9e0d12-78f4-4a56-8c01-9d2e3f4a5b6c",
                        type = SignalFilterType.RISK,
                        title = "Android13+ 권한 요청 플로우 이탈 가능성",
                    ),
                    BriefSignalItemUiModel(
                        id = "9d0a2b34-c678-4d90-8e12-3f4a5b6c7d8e",
                        type = SignalFilterType.QUESTION,
                        title = "푸시 알림 정책 논의 필요",
                    ),
                    BriefSignalItemUiModel(
                        id = "1e2f3a45-b789-4c01-9d23-4a5b6c7d8e9f",
                        type = SignalFilterType.QUESTION,
                        title = "로그인 유지 기간 결정 필요",
                    ),
                ),
                hasMore = true,
                canLoadMore = false,
                expanded = manyItemExpanded,
                onMoreClick = {
                    manyItemExpanded = true
                },
                onLoadMore = {},
                onFoldClick = {
                    manyItemExpanded = false
                },
            )
        }
    }
}
