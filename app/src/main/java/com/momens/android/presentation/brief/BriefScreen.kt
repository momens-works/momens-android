package com.momens.android.presentation.brief

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momens.android.core.common.extension.rememberMinDurationUiState
import com.momens.android.core.common.state.UiState
import com.momens.android.core.designsystem.component.header.MomensDefaultHeader
import com.momens.android.core.designsystem.theme.MomensTheme
import com.momens.android.presentation.brief.component.BriefCurrentPriority
import com.momens.android.presentation.brief.component.BriefSignalFilterSummary
import com.momens.android.presentation.brief.component.BriefSignalSummary
import com.momens.android.presentation.brief.component.BriefSummaryCard
import com.momens.android.core.model.signal.SignalFilterType
import com.momens.android.presentation.brief.model.SampleBriefUiState
import com.momens.android.presentation.brief.state.BriefUiState
import com.momens.android.presentation.loading.LoadingScreen

@Composable
fun BriefRoute(
    paddingValues: PaddingValues,
    tabBarHeight: Dp,
    viewModel: BriefViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val projectContext by viewModel.projectContext.collectAsStateWithLifecycle()

    LaunchedEffect(projectContext.projectId) {
        projectContext.projectId?.let { projectId ->
            viewModel.loadBrief(projectId = projectId)
        }
    }

    when (val renderState = rememberMinDurationUiState(uiState)) {
        UiState.Empty, UiState.Loading -> {
            LoadingScreen()
        }

        UiState.Failure -> {

        }

        is UiState.Success -> BriefScreen(
            paddingValues = paddingValues,
            bottomContentPadding = tabBarHeight,
            uiState = renderState.data,
            avatarUrl = projectContext.avatarUrl,
            onFilterClick = viewModel::selectSignalFilter,
            onSummaryMoreClick = viewModel::loadMoreSignalSummary,
            onSummaryFoldClick = viewModel::foldSignalSummary,
        )
    }
}

@Composable
private fun BriefScreen(
    paddingValues: PaddingValues,
    bottomContentPadding: Dp,
    uiState: BriefUiState,
    avatarUrl: String?,
    onFilterClick: (SignalFilterType) -> Unit,
    onSummaryMoreClick: () -> Unit,
    onSummaryFoldClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MomensTheme.colors.uiBg)
            .padding(paddingValues),
    ) {
        MomensDefaultHeader(
            onProfileClick = { },
            backgroundColor = MomensTheme.colors.uiBg,
            avatarUrl = avatarUrl,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "오늘의 브리프",
                color = MomensTheme.colors.black,
                style = MomensTheme.typography.titleB20,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(10.dp))

            BriefSummaryCard(
                title = uiState.project.name,
                targetDate = uiState.project.targetDate,
                progress = uiState.project.progress,
                summary = uiState.project.summary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            BriefSignalSummary(
                count = uiState.signalSummary.totalCount,
                content = uiState.signalSummary.summary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            BriefSignalFilterSummary(
                selectedFilterType = uiState.signalSummary.selectedFilterType,
                filters = uiState.signalSummary.filters,
                summaries = uiState.signalSummary.items,
                hasMoreSummaries = uiState.signalSummary.hasMore,
                canLoadMoreSummaries = uiState.signalSummary.hasNextPage,
                isSummaryExpanded = uiState.signalSummary.isExpanded,
                onFilterClick = onFilterClick,
                onSummaryMoreClick = onSummaryMoreClick,
                onSummaryFoldClick = onSummaryFoldClick,
            )

            Spacer(modifier = Modifier.height(24.dp))

            BriefCurrentPriority(
                priorities = uiState.priorities,
            )

            Spacer(modifier = Modifier.height(45.dp))

            Spacer(modifier = Modifier.height(bottomContentPadding))

        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFEFF1F1)
@Composable
private fun BriefScreenPreview() {
    MomensTheme {
        BriefScreen(
            paddingValues = PaddingValues(),
            bottomContentPadding = 20.dp,
            uiState = SampleBriefUiState,
            avatarUrl = null,
            onFilterClick = {},
            onSummaryMoreClick = {},
            onSummaryFoldClick = {},
        )
    }
}
