# Compose UI 흐름

이 문서는 화면·Composable·디자인 시스템·Preview·Loading/Error UI를 변경할 때 읽습니다.

## 디자인 시스템 우선

- Button, TextField, Header, Chip, Loading, BottomSheet는 기존 공통 컴포넌트를 먼저
  확인합니다.
- 색상, typography, shape는 `MomensTheme`과 디자인 토큰을 사용합니다.
- 새 색상·typography·반복 UI가 필요하면 화면에서 하드코딩하기 전에 디자인 시스템 확장을
  검토합니다.
- 재사용 Component는 `modifier: Modifier = Modifier`를 제공하고, 내부에서
  `fillMaxSize()`를 강제하지 않습니다.

## Loading, Empty, Error

- 전체 화면 API 상태는 공통 `LoadingScreen`과 명확한 Failure UI로 표현합니다.
- `UiState.Failure`를 빈 블록으로 남기지 않습니다.
- 이미 성공 데이터를 표시하는 화면의 부분 요청은 전체 `UiState`를 Loading/Failure로
  바꾸지 않습니다. 해당 섹션의 loading/error 상태를 별도로 모델링합니다.
- 빈 결과, 요청 중, 실패 상태를 같은 UI로 보이게 하지 않습니다. 디자인이 필요한 경우
  구현 범위를 넓히지 말고 티켓으로 분리합니다.

## Preview와 Fake State

- Preview는 `private`, `MomensTheme` 적용, fake state 사용을 기본으로 합니다.
- Preview에서 실제 ViewModel, Repository, Network, Hilt를 사용하지 않습니다.
- `Screen`은 fake data가 아니라 `XxxUiState`를 받고, fake는 companion object 또는
  Preview 근처 private 값으로 관리합니다.

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

## 리스트

- UiState에 노출되는 리스트는 `ImmutableList`를 우선합니다.
- `LazyColumn`, `LazyRow`, grid의 동적 항목에는 안정적인 key를 제공합니다.
- 데이터가 큰 목록은 화면 밖 항목을 불필요하게 조합하지 않는지 확인합니다.
