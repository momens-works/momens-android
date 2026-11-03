# Momens Compose Flow

Momens 프로젝트에서 Compose 화면을 작성할 때 따르는 팀 규칙입니다.

이 문서는 일반적인 Compose 작성법보다 Momens의 패키지 구조, MVVM 흐름, 디자인 시스템 사용 방식을 더 구체적으로 맞추기 위한 기준입니다.

> 예시는 프로젝트 패키지에 이미 잡혀 있는 `signal`, `project`, `brief`, `signin`, `splash`, `main` 도메인을 기준으로 작성합니다. 아직 구현되지 않은 화면을 설명할 때도 현재 구조와 맞지 않는 임의 도메인명은 피합니다.

---

## 1. Hilt ViewModel 사용

Momens의 ViewModel은 Hilt 기반으로 작성합니다.

```kotlin
@HiltViewModel
class SignalViewModel @Inject constructor(
    private val repository: SignalRepository,
) : ViewModel()
```

Composable의 `Route` 계층에서는 `hiltViewModel()`을 사용합니다.

아래 예시는 API 상태 래퍼를 쓰지 않고 `StateFlow<SignalUiState>`를 직접 노출하는 간단한 화면 기준입니다. API 호출 상태를 `UiState<SignalUiState>`로 감싸는 화면은 3번의 상태 분기 예시처럼 처리합니다.

```kotlin
@Composable
fun SignalRoute(
    navigateToProject: (Long) -> Unit,
    viewModel: SignalViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignalScreen(
        uiState = uiState,
        onSignalClick = navigateToProject,
    )
}
```

`Screen`이나 `Component` 계층에서는 ViewModel을 직접 참조하지 않습니다.

---

## 2. Route / Screen / Component 역할 분리

화면은 가능하면 `Route`, `Screen`, `Component` 계층으로 나눕니다.

| 계층 | 역할 |
| --- | --- |
| `XxxRoute` | ViewModel 연결, UiState 수집, SideEffect 처리, navigation callback 연결 |
| `XxxScreen` 또는 `XxxContent` | 상태와 이벤트를 파라미터로 받아 UI만 그림 |
| `Component` | 재사용 가능한 UI 단위, ViewModel/NavController 직접 참조 금지 |

### Route

아래 예시는 API 상태 래퍼를 쓰지 않고 `StateFlow<SignalUiState>`를 직접 노출하는 화면 기준입니다.

```kotlin
@Composable
fun SignalRoute(
    navigateToProject: (Long) -> Unit,
    viewModel: SignalViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignalScreen(
        uiState = uiState,
        onSignalClick = navigateToProject,
    )
}
```

### Screen

```kotlin
@Composable
fun SignalScreen(
    uiState: SignalUiState,
    onSignalClick: (Long) -> Unit,
) {
    SignalContent(
        signals = uiState.signals,
        onSignalClick = onSignalClick,
    )
}
```

### Component

```kotlin
@Composable
fun SignalCard(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Column {
            Text(text = title)
            Text(text = summary)
        }
    }
}
```

---

## 3. 공통 UiState 패턴

API 호출 결과를 표현하는 화면은 `Loading`, `Success`, `Failure` 상태를 명확히 분리합니다.

공통 상태는 `core/common/state` 패키지에 둡니다.

```kotlin
sealed interface UiState<out T> {
    data object Empty : UiState<Nothing>
    data object Loading : UiState<Nothing>

    data class Success<T>(
        val data: T,
    ) : UiState<T>

    data object Failure : UiState<Nothing>
}
```

ViewModel에서는 화면 전용 상태를 `UiState<XxxUiState>` 형태로 감싸서 관리할 수 있습니다.

```kotlin
private val _uiState = MutableStateFlow<UiState<SignalUiState>>(UiState.Loading)
val uiState: StateFlow<UiState<SignalUiState>> = _uiState.asStateFlow()
```

Route에서는 상태별 UI를 명확히 분리합니다.

```kotlin
when (val state = uiState) {
    UiState.Loading -> {
        LoadingScreen()
    }

    UiState.Failure -> {
        ErrorContent(
            onRetryClick = viewModel::loadSignals,
        )
    }

    is UiState.Success -> {
        SignalScreen(
            uiState = state.data,
            onSignalClick = viewModel::onSignalClick,
        )
    }

    UiState.Empty -> Unit
}
```

---

## 4. SideEffect 처리 규칙

Navigation, Toast, Snackbar, Dialog 표시처럼 한 번만 발생해야 하는 이벤트는 `UiState`에 넣지 않고 `SideEffect`로 분리합니다.

```kotlin
sealed interface SignalSideEffect {
    data class NavigateToProject(
        val projectId: Long,
    ) : SignalSideEffect

    data class ShowToast(
        val message: String,
    ) : SignalSideEffect
}
```

ViewModel에서는 `MutableSharedFlow`로 SideEffect를 노출합니다.

```kotlin
private val _sideEffect = MutableSharedFlow<SignalSideEffect>()
val sideEffect: SharedFlow<SignalSideEffect> = _sideEffect.asSharedFlow()
```

Route 계층에서 SideEffect를 수집하고 처리합니다.

```kotlin
viewModel.sideEffect.collectLatestSideEffect { sideEffect ->
    when (sideEffect) {
        is SignalSideEffect.NavigateToProject -> {
            navigateToProject(sideEffect.projectId)
        }

        is SignalSideEffect.ShowToast -> {
            showToast(sideEffect.message)
        }
    }
}
```

단, `collectLatestSideEffect`는 프로젝트에 해당 확장 함수가 존재할 때 사용합니다. 확장 함수가 없다면 `LaunchedEffect` 기반 수집 방식으로 작성합니다.

---

## 5. MomensTheme 사용

색상, typography, shape는 프로젝트 Theme과 디자인 시스템을 우선 사용합니다.

현재 `MomensTheme`은 Material3 `MaterialTheme`을 감싸는 형태입니다. 디자인 토큰이 확장되기 전에는 `MaterialTheme.colorScheme`, `MaterialTheme.typography`를 사용하고, 프로젝트 고유 토큰이 추가되면 `MomensTheme.colors`, `MomensTheme.typography` 같은 팀 컨벤션으로 맞춥니다.

### 현재 구조에서 권장

```kotlin
Text(
    text = title,
    color = MaterialTheme.colorScheme.onSurface,
    style = MaterialTheme.typography.titleMedium,
)
```

### 디자인 시스템 확장 후 권장

```kotlin
Text(
    text = title,
    color = MomensTheme.colors.gray1000,
    style = MomensTheme.typography.title1SB18,
)
```

### 피해야 하는 예시

```kotlin
Text(
    text = title,
    color = Color.Black,
    fontSize = 18.sp,
    fontWeight = FontWeight.SemiBold,
)
```

새로운 색상이나 typography가 필요하면 화면 내부에 직접 하드코딩하지 않고 디자인 시스템에 추가하는 것을 우선 고려합니다.

---

## 6. Typed Navigation 규칙

새로운 route는 단순 문자열보다 type-safe route object/data class 사용을 우선합니다.

```kotlin
@Serializable
data object Signal : MainTabRoute
```

NavController 이동은 확장 함수로 감쌉니다.

```kotlin
fun NavController.navigateToSignal(
    navOptions: NavOptions? = null,
) {
    navigate(
        route = Signal,
        navOptions = navOptions,
    )
}
```

문자열 route를 화면 내부에 직접 하드코딩하지 않습니다.

### 피해야 하는 예시

```kotlin
navController.navigate("project/$id")
```

### 권장 예시

```kotlin
navController.navigateToProject(id)
```

---

## 7. 패키지 구조 규칙

새 파일은 기존 feature 구조를 따릅니다.

```text
app/src/main/java/com/momens/android
├── core
│   ├── common
│   │   ├── extension
│   │   ├── navigation
│   │   └── state
│   ├── designsystem
│   │   ├── component
│   │   ├── theme
│   │   └── type
│   ├── local
│   ├── network
│   └── util
├── data
│   ├── brief
│   ├── project
│   ├── signal
│   └── signin
└── presentation
    ├── brief
    ├── main
    ├── project
    ├── signal
    ├── signin
    └── splash
```

feature 화면 파일은 다음처럼 둡니다.

```text
presentation/{feature}/XxxRoute.kt
presentation/{feature}/XxxScreen.kt
presentation/{feature}/XxxViewModel.kt
presentation/{feature}/XxxUiState.kt
presentation/{feature}/XxxSideEffect.kt
presentation/{feature}/component
presentation/{feature}/navigation
```

화면 전용 컴포넌트는 해당 feature의 `component` 패키지에 둡니다. 여러 화면에서 재사용되는 컴포넌트는 `core/designsystem/component` 또는 공통 component 영역으로 이동을 고려합니다.

---

## 8. API 연결 전 MockData / Preview 규칙

API 연결 전 화면을 먼저 구현할 때도 Composable 내부에 임시 데이터를 직접 하드코딩하지 않습니다.

먼저 화면 계약이 되는 `XxxUiState`와 필요한 `XxxUiModel`을 정의하고, Preview 또는 임시 화면 확인용 fake state를 만듭니다.

### 기준

- `Screen`은 fake data가 아니라 `XxxUiState`를 파라미터로 받는다.
- fake/mock data는 `XxxUiState`의 companion object 또는 Preview 근처의 private 값으로 둔다.
- fake data는 실제 API 연결 시 ViewModel의 repository 결과 매핑으로 쉽게 교체될 수 있어야 한다.
- Preview에서는 ViewModel, Repository, Network를 직접 사용하지 않는다.
- 리스트 데이터는 `ImmutableList`, `persistentListOf()`, `toImmutableList()` 사용을 우선한다.
- API 연결 전이라도 Route / Screen / Component 분리 구조는 유지한다.
- MockData는 UI 구현과 Preview 확인을 위한 용도이며, 비즈니스 로직을 대신하지 않는다.

```kotlin
@Immutable
data class SignalUiState(
    val userName: String = "",
    val signals: ImmutableList<SignalUiModel> = persistentListOf(),
) {
    companion object {
        val Fake = SignalUiState(
            userName = "혜민",
            signals = persistentListOf(
                SignalUiModel(
                    id = 1L,
                    title = "결정이 필요한 API 응답 스펙 변경",
                    summary = "프로젝트 상세 화면과 브리프 화면에 영향이 있습니다.",
                ),
                SignalUiModel(
                    id = 2L,
                    title = "오늘 안에 확인할 배포 리스크",
                    summary = "로그인 토큰 갱신 실패 케이스 확인이 필요합니다.",
                ),
            ),
        )
    }
}
```

상태가 중요한 화면은 상태별 fake를 둡니다.

```kotlin
companion object {
    val FakeNormal = SignalUiState(...)
    val FakeEmpty = SignalUiState(...)
    val FakeLongText = SignalUiState(...)
}
```

---

## 9. Preview 작성 강화

중요한 화면과 재사용 컴포넌트는 Preview를 작성합니다.

Preview는 다음 기준을 따릅니다.

- `private`로 작성한다.
- 함수 이름은 대상과 상태가 드러나게 작성한다.
- 프로젝트 Theme으로 감싼다.
- 실제 ViewModel, Repository, Network를 사용하지 않는다.
- fake UiState 또는 sample model을 사용한다.

```kotlin
@Preview(showBackground = true)
@Composable
private fun SignalScreenPreview() {
    MomensTheme {
        SignalScreen(
            uiState = SignalUiState.Fake,
            onSignalClick = {},
        )
    }
}
```

```kotlin
@Preview(showBackground = true)
@Composable
private fun SignalScreenEmptyPreview() {
    MomensTheme {
        SignalScreen(
            uiState = SignalUiState.Fake.copy(
                signals = persistentListOf(),
            ),
            onSignalClick = {},
        )
    }
}
```

가능하면 아래 상태를 Preview로 확인합니다.

- 기본 상태
- 빈 상태
- 긴 텍스트 상태
- 리스트가 많은 상태
- 선택/비선택 상태
- 비활성 상태
- 로딩 상태
- 에러 상태

---

## 10. Immutable Collection 사용

Compose UiState에서 화면에 노출되는 리스트는 가능하면 immutable collection을 사용합니다.

```kotlin
data class SignalUiState(
    val signals: ImmutableList<SignalUiModel> = persistentListOf(),
)
```

일반 `List`를 화면 상태로 변환할 때는 `toImmutableList()`를 사용합니다.

ViewModel이 `StateFlow<SignalUiState>`를 직접 노출하는 화면이라면 아래처럼 변환합니다.

```kotlin
_uiState.update {
    SignalUiState(
        signals = response.signals.toImmutableList(),
    )
}
```

ViewModel이 `StateFlow<UiState<SignalUiState>>`를 노출하는 API 화면이라면 `UiState.Success`로 감싸서 업데이트합니다.

```kotlin
_uiState.update {
    UiState.Success(
        data = SignalUiState(
            signals = response.signals.toImmutableList(),
        ),
    )
}
```

이 규칙은 Compose에서 상태 안정성을 높이고, 리스트 변경 시 불필요한 혼란을 줄이기 위한 목적입니다.

---

## 11. 디자인 시스템 컴포넌트 우선 사용

Button, TextField, TopAppBar, Chip, Loading, BottomSheet 같은 UI는 직접 Material 컴포넌트를 꾸미기 전에 프로젝트의 기존 공통 컴포넌트를 먼저 확인합니다.

현재 공통 컴포넌트가 없다면 화면 내부 구현으로 시작할 수 있지만, 두 개 이상의 화면에서 반복되거나 팀 디자인 토큰이 필요한 경우 `core/designsystem/component`로 이동합니다.

---

## 12. Loading / Failure UI 기준

API 결과를 보여주는 화면은 Loading, Success, Failure 처리를 명확히 합니다.

- Loading 상태는 공통 Loading 컴포넌트 사용을 우선한다.
- Failure 상태를 빈 블록으로 방치하지 않는다.
- 아직 디자인이 없다면 TODO 주석으로 의도를 남기거나 최소 안내 UI를 작성한다.
- Success 상태에서는 이미 ViewModel에서 가공된 UiState를 사용한다.

```kotlin
when (val state = uiState) {
    UiState.Loading -> {
        LoadingScreen()
    }

    UiState.Failure -> {
        ErrorContent(
            onRetryClick = viewModel::loadSignals,
        )
    }

    is UiState.Success -> {
        SignalScreen(
            uiState = state.data,
            onSignalClick = viewModel::onSignalClick,
        )
    }

    UiState.Empty -> Unit
}
```

Failure 상태를 아래처럼 비워두지 않습니다.

```kotlin
UiState.Failure -> Unit
```

---

## 13. `.editorconfig` / ktlint 적용

코드 생성 전후에는 프로젝트 루트의 `.editorconfig`와 ktlint 설정을 기준으로 포맷을 맞춥니다.

- indentation, max line length, final newline, trailing whitespace 규칙을 따른다.
- 사용하지 않는 import를 제거한다.
- wildcard import를 피한다.
- ktlint 규칙과 `.editorconfig`가 충돌하면 ktlint 검사 결과를 우선 확인한다.

검사 명령어:

```bash
./gradlew ktlintCheck
```

자동 포맷:

```bash
./gradlew ktlintFormat
```

### 운영체제별 실행과 문제 확인

Gradle Wrapper를 사용하면 ktlint와 Gradle 버전이 팀원마다 달라지는 것을 막을 수 있습니다.

| 환경 | 검사 | 자동 포맷 |
| --- | --- | --- |
| macOS / Linux 터미널 | `./gradlew :app:ktlintCheck` | `./gradlew :app:ktlintFormat` |
| Windows PowerShell / 명령 프롬프트 | `.\gradlew.bat :app:ktlintCheck` | `.\gradlew.bat :app:ktlintFormat` |
| Windows Git Bash | `./gradlew :app:ktlintCheck` | `./gradlew :app:ktlintFormat` |

처음 실행하는 환경에서는 Gradle Wrapper와 Java 21 toolchain을 내려받을 수 있으므로
인터넷 연결이 필요합니다. 사내·학교 네트워크, 프록시 또는 오프라인 환경에서는 다운로드가
실패해 ktlint 작업까지 도달하지 못할 수 있습니다.

ktlint가 실행되지 않으면 다음처럼 stacktrace를 포함해 실행하고, 실패 메시지의 앞부분을
공유합니다.

```bash
# macOS / Linux / Windows Git Bash
./gradlew :app:ktlintCheck --stacktrace

# Windows PowerShell / 명령 프롬프트
.\gradlew.bat :app:ktlintCheck --stacktrace
```

다음 메시지는 ktlint 규칙 문제가 아니라 개발 환경 문제일 가능성이 높습니다.

- `JAVA_HOME`, `toolchain`: Java 21 탐색 또는 다운로드 실패
- `Could not resolve`, `SSL`, `proxy`: Gradle 또는 toolchain 다운로드 네트워크 문제
- `Access denied`: Gradle 캐시 또는 프로젝트 파일 권한 문제

Android Studio의 **Reformat Code** 결과가 ktlint와 항상 같지는 않습니다. 프로젝트 루트의
`.editorconfig`를 적용하되, 최종 포맷 기준은 Gradle `ktlintCheck` 결과로 통일합니다.

---

## 14. API 연결 전 작업 흐름

API 연결 전 화면 구현은 다음 순서로 진행합니다.

1. 화면에 필요한 `XxxUiState`와 `XxxUiModel`을 먼저 정의한다.
2. Preview 확인용 fake 또는 상태별 fake state를 만든다.
3. `XxxScreen`은 fake가 아니라 `XxxUiState`와 event lambda를 받도록 작성한다.
4. 필요한 재사용 UI는 `component`로 분리한다.
5. Preview로 기본/빈/긴 텍스트/선택 상태를 확인한다.
6. API 연결 시 ViewModel에서 repository 응답을 `XxxUiState`로 매핑한다.
7. Composable 내부의 fake/mock 데이터는 남기지 않는다.

### 피해야 하는 예시

```kotlin
@Composable
fun SignalScreen() {
    val signals = listOf(
        SignalUiModel(
            id = 1L,
            title = "임시 데이터",
            summary = "임시 요약",
        ),
    )

    SignalContent(signals = signals)
}
```

### 권장 예시

```kotlin
@Composable
fun SignalScreen(
    uiState: SignalUiState,
    onSignalClick: (Long) -> Unit,
) {
    SignalContent(
        signals = uiState.signals,
        onSignalClick = onSignalClick,
    )
}
```

Preview에서는 fake state를 주입합니다.

```kotlin
@Preview(showBackground = true)
@Composable
private fun SignalScreenPreview() {
    MomensTheme {
        SignalScreen(
            uiState = SignalUiState.Fake,
            onSignalClick = {},
        )
    }
}
```

---

## 15. Momens 고유 흐름 요약

Momens 프로젝트에서는 다음 흐름을 우선합니다.

1. ViewModel은 Hilt 기반으로 작성한다.
2. 화면은 Route / Screen / Component 계층으로 분리한다.
3. Route에서 ViewModel 연결, UiState 수집, SideEffect 처리를 담당한다.
4. Screen은 UiState와 event lambda를 받아 UI만 그린다.
5. Component는 재사용 가능한 UI 단위로 작성한다.
6. API 상태는 `UiState<T>`로 표현한다.
7. Navigation, Toast, Snackbar 같은 일회성 이벤트는 SideEffect로 분리한다.
8. 색상과 typography는 디자인 시스템을 우선 사용한다.
9. Navigation은 Typed Navigation과 확장 함수를 우선 사용한다.
10. 새 파일은 기존 feature 패키지 구조를 따른다.
11. API 연결 전에도 UiState와 fake state를 먼저 만들고 화면을 구현한다.
12. Preview에서는 실제 의존성을 사용하지 않는다.
