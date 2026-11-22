# Compose 화면 구현 흐름

이 문서는 새 화면 또는 API 연결 전 UI를 구현할 때 읽습니다.

## 구현 순서

1. `XxxUiState`와 필요한 `XxxUiModel`을 정의합니다.
2. 기본·빈·긴 텍스트 등 확인에 필요한 fake state를 만듭니다.
3. `XxxRoute`에서 ViewModel과 lifecycle-aware state 수집을 연결합니다.
4. `XxxScreen` 또는 `XxxContent`는 UiState와 event lambda만 받아 렌더링합니다.
5. 재사용 단위만 `component/`로 분리합니다.
6. Preview에서 대표 상태를 확인합니다.
7. API 연결 시 Repository 응답을 ViewModel에서 UiState로 매핑합니다.
8. Composable 내부의 임시 mock data를 제거합니다.

## Fake State 원칙

- fake/mock data는 UI 계약과 Preview 확인을 위한 용도입니다.
- Composable 내부에서 임시 리스트를 만들거나 비즈니스 로직을 대신하지 않습니다.
- 실제 API 연결 시 fake를 Repository 응답 매핑으로 쉽게 교체할 수 있어야 합니다.

```kotlin
@Immutable
data class SignalUiState(
    val signals: ImmutableList<SignalUiModel> = persistentListOf(),
) {
    companion object {
        val Fake = SignalUiState(
            signals = persistentListOf(
                SignalUiModel(
                    id = 1L,
                    title = "예시 신호",
                    summary = "Preview 확인용 데이터",
                ),
            ),
        )
    }
}
```

## 구현 전 확인

- 기존 feature의 Route, Screen, ViewModel, component 분리 방식을 먼저 확인합니다.
- 새 디자인 시스템 요소보다 기존 공통 컴포넌트를 우선 사용합니다.
- Loading, Empty, Error, Success 중 이 화면에 필요한 상태를 먼저 정합니다.
