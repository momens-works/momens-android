package com.momens.android.presentation.brief.model

import com.momens.android.core.model.signal.SignalFilterType
import com.momens.android.presentation.brief.state.BriefUiState
import kotlinx.collections.immutable.persistentListOf

val SampleBriefUiState = BriefUiState(
    project = BriefProjectUiModel(
        id = "30d9e9fe-f43b-4097-a88e-dc19f0a5b025",
        name = "Q2 Activation Readiness",
        targetDate = "6월 30일",
        progress = 0.64f,
        summary = "목표일까지 Q2 Activation Readiness 범위의 " +
            "회원 가입 MVP를 안정적으로 릴리즈한다.",
    ),
    signalSummary = BriefSignalSummaryUiModel(
        summary = "Android 권한 요청 이슈가 발견되었으며, 소셜 로그인은 MVP 범위에서 제외되었습니다. " +
            "이메일 회원가입과 온보딩 이탈 개선이 우선적으로 필요합니다.",
        filters = persistentListOf(
            BriefSignalSummaryFilter(
                type = SignalFilterType.ALL,
                label = "All",
                count = 6,
            ),
            BriefSignalSummaryFilter(
                type = SignalFilterType.CHANGE,
                label = "Change",
                count = 1,
            ),
            BriefSignalSummaryFilter(
                type = SignalFilterType.RISK,
                label = "Risk",
                count = 1,
            ),
            BriefSignalSummaryFilter(
                type = SignalFilterType.DECISION,
                label = "Decision",
                count = 2,
            ),
            BriefSignalSummaryFilter(
                type = SignalFilterType.QUESTION,
                label = "Question",
                count = 2,
            ),
        ),
        selectedFilterType = SignalFilterType.ALL,
        items = persistentListOf(
            BriefSignalItemUiModel(
                id = "5c1a2b34-56d7-4e89-9f01-234a5b6c7d8e",
                type = SignalFilterType.CHANGE,
                title = "권한 요청 반복 문의",
            ),
            BriefSignalItemUiModel(
                id = "6f3d8a61-4de7-4c01-9d2b-16fdf182e9a1",
                type = SignalFilterType.DECISION,
                title = "소셜 로그인은 MVP 범위에서 제외",
            ),
            BriefSignalItemUiModel(
                id = "3b9e0d12-78f4-4a56-8c01-9d2e3f4a5b6c",
                type = SignalFilterType.RISK,
                title = "Android 13+ 권한 요청 플로우 이탈 가능성",
            ),
            BriefSignalItemUiModel(
                id = "27afd507-9c7f-4f0d-a2be-fcdab2477b19",
                type = SignalFilterType.DECISION,
                title = "회원가입 MVP 범위 1차 확정",
            ),
            BriefSignalItemUiModel(
                id = "9d0a2b34-c678-4d90-8e12-3f4a5b6c7d8e",
                type = SignalFilterType.QUESTION,
                title = "온보딩 이탈 구간 계측 필요",
            ),
            BriefSignalItemUiModel(
                id = "1e2f3a45-b789-4c01-9d23-4a5b6c7d8e9f",
                type = SignalFilterType.QUESTION,
                title = "권한 요청 전 안내 화면 추가 여부",
            ),
        ),
        nextCursor = null,
        isExpanded = false,
    ),
    priorities = persistentListOf(
        BriefPriorityUiModel(
            rank = 1,
            title = "이메일 회원가입 완료율 개선",
            taskId = "27afd507-9c7f-4f0d-a2be-fcdab2477b19",
        ),
        BriefPriorityUiModel(
            rank = 2,
            title = "Android 13+ 권한 요청 플로우 정비",
            taskId = "4c8e1f23-a567-4b89-9c01-2d3e4f5a6b7c",
        ),
        BriefPriorityUiModel(
            rank = 3,
            title = "온보딩 이탈 구간 계측 추가",
            taskId = "9d0a2b34-c678-4d90-8e12-3f4a5b6c7d8e",
        ),
        BriefPriorityUiModel(
            rank = 4,
            title = "소셜 로그인 제외 범위 QA",
            taskId = "1e2f3a45-b789-4c01-9d23-4a5b6c7d8e9f",
        ),
    ),
)
