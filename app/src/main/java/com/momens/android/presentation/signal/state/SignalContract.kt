package com.momens.android.presentation.signal.state

import androidx.compose.runtime.Immutable
import com.momens.android.core.designsystem.component.type.SignalTagType
import com.momens.android.presentation.signal.model.SignalAccordionType
import com.momens.android.presentation.signal.model.SignalCardUiModel
import com.momens.android.presentation.signal.model.SignalEvidenceUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class SignalState(
    val pageTitle: String = "오늘 확인해야 할 시그널",
    val pageDescription: String = "프로젝트의 의사결정에 영향을 줄 수 있는 변화입니다.",
    val signals: ImmutableList<SignalCardUiModel> = persistentListOf(),
    val evidencesBySignalId: ImmutableMap<String, ImmutableList<SignalEvidenceUiModel>> = persistentMapOf(),
) {
    companion object {
        val Fake = SignalState(
            signals = persistentListOf(
                SignalCardUiModel(
                    id = "1",
                    type = SignalTagType.RISK,
                    title = "Android 13+ 권한 요청 플로우에서 이탈 가능성 발견",
                    impact = "MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                    minsuSuggestion = "내용이 들어갈 공간입니다",
                ),
                SignalCardUiModel(
                    id = "2",
                    type = SignalTagType.CHANGE,
                    title = "Android 13+ 권한 요청 플로우에서 이탈 가능성 발견",
                    impact = "MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                    minsuSuggestion = "내용이 들어갈 공간입니다",
                ),
                SignalCardUiModel(
                    id = "3",
                    type = SignalTagType.CHANGE,
                    title = "Android 13+ 권한 요청 플로우에서 이탈 가능성 발견",
                    impact = "MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                    minsuSuggestion = "내용이 들어갈 공간입니다",
                ),
                SignalCardUiModel(
                    id = "4",
                    type = SignalTagType.RISK,
                    title = "Android 13+ 권한 요청 플로우에서 이탈 가능성 발견",
                    impact = "MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                    minsuSuggestion = "내용이 들어갈 공간입니다",
                ),
                SignalCardUiModel(
                    id = "5",
                    type = SignalTagType.CHANGE,
                    title = "Android 13+ 권한 요청 플로우에서 이탈 가능성 발견",
                    impact = "MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                    minsuSuggestion = "내용이 들어갈 공간입니다",
                ),
                SignalCardUiModel(
                    id = "6",
                    type = SignalTagType.CHANGE,
                    title = "Android 13+ 권한 요청 플로우에서 이탈 가능성 발견",
                    impact = "MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                    minsuSuggestion = "내용이 들어갈 공간입니다",
                ),
            ),
            evidencesBySignalId = persistentMapOf(
                "1" to persistentListOf(
                    SignalEvidenceUiModel(
                        sourceRefId = "11",
                        source = SignalAccordionType.FIGMA,
                        time = "00분 전",
                        target = "권한 요청 화면",
                        change = "권한 요청 단계 이탈률이 오른 것으로 보임",
                        impact = "회원가입 완료율이 떨어질 수 있음",
                    ),
                    SignalEvidenceUiModel(
                        sourceRefId = "12",
                        source = SignalAccordionType.FILE,
                        time = "00분 전",
                        target = "권한 요청 화면 기획서",
                        change = "권한 요청 문구가 변경됨",
                        impact = "요청 이해도에 영향을 줄 수 있음",
                    ),
                    SignalEvidenceUiModel(
                        sourceRefId = "13",
                        source = SignalAccordionType.GITHUB,
                        time = "00분 전",
                        target = "권한 요청 PR",
                        change = "런타임 권한 분기 로직 추가됨",
                        impact = "테스트 커버리지 확인 필요",
                    ),
                ),
                "2" to persistentListOf(
                    SignalEvidenceUiModel(
                        sourceRefId = "21",
                        source = SignalAccordionType.SLACK,
                        time = "12분 전",
                        target = "#release 채널",
                        change = "권한 정책 변경 공지",
                        impact = "QA 일정 조정 필요",
                    ),
                    SignalEvidenceUiModel(
                        sourceRefId = "22",
                        source = SignalAccordionType.GITHUB,
                        time = "00분 전",
                        target = "권한 요청 PR",
                        change = "런타임 권한 분기 로직 추가됨",
                        impact = "테스트 커버리지 확인 필요",
                    ),
                ),
            ),
        )
    }
}

sealed interface SignalSideEffect {
    data class ShowActionSnackbar(
        val message: String,
        val description: String,
        val taskId: String,
    ) : SignalSideEffect

    data class ShowSnackbar(val message: String) : SignalSideEffect
}
