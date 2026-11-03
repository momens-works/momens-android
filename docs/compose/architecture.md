# Compose 아키텍처 흐름

이 문서는 ViewModel, Route/Screen/Component, 화면 상태, SideEffect, Navigation,
패키지 구조를 변경할 때 읽습니다.

## ViewModel과 상태

- ViewModel은 Hilt로 생성하고 화면 상태는 `StateFlow`로 노출합니다.
- 서버 데이터는 화면 전용 `XxxUiState`와 `XxxUiModel`으로 매핑합니다.
- API 결과가 있는 화면은 `UiState.Loading`, `Success`, `Failure`, `Empty`를 명확히
  구분합니다.
- 화면에 노출하는 리스트는 가능하면 `ImmutableList`로 관리하고, 응답 리스트는
  `toImmutableList()`로 변환합니다.

```kotlin
@HiltViewModel
class SignalViewModel @Inject constructor(
    private val repository: SignalRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<UiState<SignalUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<SignalUiState>> =
        _uiState.asStateFlow()
}

```

## Route, Screen, Component

| Layer | Responsibility |
| --- | --- |
| `XxxRoute` | ViewModel 연결, lifecycle-aware state 수집, SideEffect, navigation callback 연결 |
| `XxxScreen` / `XxxContent` | UiState와 event lambda를 받아 UI만 렌더링 |
| `component` | 재사용 UI. ViewModel, Repository, NavController 직접 참조 금지 |

Route에서는 `collectAsStateWithLifecycle()`을 사용합니다. Screen과 Component는 상태를
아래로 받고 이벤트를 lambda로 위로 보냅니다.

## SideEffect

Navigation, Toast, Snackbar, Dialog처럼 한 번만 처리할 이벤트는 UiState가 아니라
`SharedFlow<SideEffect>`로 분리합니다. Route에서 수집해 navigation 또는 UI event를
처리합니다.

## Navigation

- 단순 문자열 route보다 type-safe route object 또는 data class를 우선합니다.
- `NavController` 호출은 navigation 확장 함수로 감쌉니다.
- Screen과 Component 안에 route 문자열 또는 `NavController`를 두지 않습니다.

## 패키지 위치

```text
presentation/{feature}/
├── XxxRoute.kt
├── XxxScreen.kt
├── XxxViewModel.kt
├── XxxUiState.kt
├── XxxSideEffect.kt
├── component/
└── navigation/
```

여러 화면이 공유하는 UI는 `core/designsystem/component`을 우선 검토하고, 화면 전용 UI는
해당 feature의 `component`에 둡니다.
