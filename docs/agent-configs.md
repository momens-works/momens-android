# Agent Configs

Momens Android 프로젝트에서 사용하는 AI 에이전트 설정을 설명합니다.

이 문서는 에이전트 설정 파일을 직접 읽지 않아도, 어떤 에이전트를 언제 어떻게 사용하면 되는지 알 수 있도록 정리한 사용자용 가이드입니다.

## 문서 구조

```text
.codex/agents/
├── momens-explorer.toml
├── momens-reviewer.toml
├── code-reviewer.toml
└── pr-writer.toml

.claude/agents/
├── momens-explorer.md
├── momens-reviewer.md
├── code-reviewer.md
└── pr-writer.md
```

Codex와 Claude Code 폴더에는 같은 역할의 에이전트 설정이 들어 있습니다.

Codex는 TOML 설정을 사용하고, Claude Code는 YAML frontmatter가 있는 Markdown 설정을 사용합니다.

Claude Code는 `.claude/agents/` 안의 Markdown 파일만 프로젝트 에이전트로 자동 등록합니다. 따라서 해당 폴더에서는 `.toml`이 아닌 `.md` 파일을 사용합니다.

## 에이전트 목록

| 에이전트 | Codex 파일 | Claude Code 파일 | 목적 |
| --- | --- | --- | --- |
| `momens_explorer` | `momens-explorer.toml` | `momens-explorer.md` | 구현 전에 관련 코드 구조와 흐름을 읽기 전용으로 탐색 |
| `momens_reviewer` | `momens-reviewer.toml` | `momens-reviewer.md` | Momens Compose 규칙을 기준으로 화면 구현을 리뷰 |
| `code_reviewer` | `code-reviewer.toml` | `code-reviewer.md` | PR 리뷰처럼 correctness, architecture, test risk를 넓게 점검 |
| `pr_writer` | `pr-writer.toml` | `pr-writer.md` | PR 템플릿에 맞춰 현재 변경사항의 PR 본문을 작성 |

네 에이전트는 모두 읽기 전용입니다. Claude Code 설정은 `Read`, `Glob`, `Grep`, `Bash`만 허용하고 `permissionMode: plan`을 사용하므로 파일을 수정하거나 쓰지 않습니다.

파일을 직접 수정하지 않고, 코드 구조를 설명하거나 리뷰 의견을 제공합니다.

## 선택 기준

| 상황 | 사용할 에이전트 |
| --- | --- |
| 어떤 파일을 봐야 할지 모르겠음 | `momens_explorer` |
| 구현 전에 기존 화면 흐름을 알고 싶음 | `momens_explorer` |
| Compose 화면이 Momens 규칙을 지키는지 보고 싶음 | `momens_reviewer` |
| ViewModel, UiState, SideEffect 구조를 점검하고 싶음 | `momens_reviewer` |
| PR 올리기 전 버그 가능성과 테스트 누락을 보고 싶음 | `code_reviewer` |
| 변경사항 전체를 더 엄격하게 리뷰하고 싶음 | `code_reviewer` |
| PR 템플릿에 맞는 본문 초안이 필요함 | `pr_writer` |
| 변경사항을 리뷰어가 읽기 좋은 PR 설명으로 정리하고 싶음 | `pr_writer` |

## 기본 사용 흐름

새 기능이나 화면을 작업할 때는 보통 다음 순서로 사용합니다.

```text
1. momens_explorer
   기존 구조, 관련 파일, 상태 흐름, navigation 흐름을 파악합니다.

2. 구현
   일반 작업 에이전트나 작업자가 코드를 수정합니다.

3. momens_reviewer
   Momens Compose 규칙을 기준으로 화면 구조를 점검합니다.

4. code_reviewer
   PR 리뷰처럼 버그 가능성, architecture 문제, missing tests를 확인합니다.

5. pr_writer
   `.github/pull_request_template.md` 형식에 맞춰 PR 본문을 작성합니다.
```

항상 네 단계를 모두 거칠 필요는 없습니다.

작은 UI 수정이라면 `momens_reviewer`만으로 충분할 수 있고, 단순 구조 파악이 목적이면 `momens_explorer`만 사용해도 됩니다.

PR 본문만 필요하다면 `pr_writer`만 사용해도 됩니다.

## 요청 방법

에이전트를 사용할 때는 에이전트 이름과 확인할 범위를 같이 말합니다.

좋은 요청에는 보통 다음 정보가 들어갑니다.

- 화면 이름 또는 기능 이름
- 관련 패키지나 파일 경로
- 보고 싶은 관점
- 현재 고민되는 부분

## 요청 예시

### 구현 전 탐색

```text
momens_explorer로 signin 화면 구조를 탐색해줘.
관련 파일, ViewModel, Route, Screen, navigation 흐름을 정리해줘.
```

```text
momens_explorer로 app/src/main/java/com/momens/android/presentation/project 패키지를 봐줘.
데이터가 repository에서 UI까지 어떻게 흐르는지 알려줘.
```

### Compose 규칙 리뷰

```text
momens_reviewer로 현재 변경한 Compose 화면을 리뷰해줘.
Route / Screen / Component 분리, UiState, SideEffect, Preview 위주로 봐줘.
```

```text
momens_reviewer로 signin 관련 변경사항을 봐줘.
Momens Compose 규칙을 어긴 부분만 중요도 순으로 알려줘.
```

### 최종 코드 리뷰

```text
code_reviewer로 현재 git diff를 PR 리뷰처럼 봐줘.
Critical, Major, Minor 순서로 정리하고 missing tests도 알려줘.
```

```text
code_reviewer로 project 패키지 변경사항을 리뷰해줘.
버그 가능성, architecture 문제, navigation 문제, null-safety 문제를 우선해서 봐줘.
```

### PR 본문 작성

```text
pr_writer로 현재 git diff를 기준으로 PR 본문 작성해줘.
관련 Momens 티켓은 MOM-0752이고, 테스트는 아직 안 돌렸어.
```

```text
pr_writer로 현재 변경사항을 `.github/pull_request_template.md`에 맞춰 정리해줘.
모르는 항목은 추측하지 말고 확인 필요로 남겨줘.
```

## 에이전트별 역할

### `momens_explorer`

구현 전에 코드베이스를 읽고 관련 구조를 정리합니다.

주로 확인하는 것:

- 관련 파일과 패키지
- Composable, ViewModel, repository, navigation 위치
- UiState가 만들어지고 전달되는 흐름
- 이벤트가 람다나 SideEffect로 올라가는 흐름
- 수정해야 할 가능성이 있는 파일

결과는 보통 관련 파일, 현재 흐름, 중요한 symbol, 위험하거나 불명확한 점, 다음에 볼 파일 순서로 정리됩니다.

### `momens_reviewer`

Momens Android Compose 규칙을 기준으로 구현을 리뷰합니다.

주로 확인하는 것:

- Route / Screen / Component 분리
- ViewModel의 상태 소유
- `collectAsStateWithLifecycle()` 사용
- UiState와 SideEffect 분리
- reusable component의 `modifier: Modifier = Modifier`
- ViewModel 또는 NavController 직접 참조 여부
- Preview와 MomensTheme 사용

Compose 화면 구현 후 가장 먼저 돌려보기 좋은 리뷰 에이전트입니다.

### `code_reviewer`

PR 리뷰에 가까운 넓은 관점으로 변경사항을 점검합니다.

주로 확인하는 것:

- 실제 버그 가능성
- architecture와 책임 분리 문제
- lifecycle, navigation, recomposition 위험
- null-safety와 edge case
- 테스트 또는 수동 검증 누락
- ktlint와 `.editorconfig` 규칙

단순 컨벤션 확인보다 최종 점검에 더 적합합니다.

### `pr_writer`

PR 템플릿에 맞춰 현재 변경사항의 PR 본문을 작성합니다.

주로 확인하는 것:

- `.github/pull_request_template.md`
- 현재 `git diff`
- 현재 브랜치명에 포함된 Momens 티켓 번호
- 변경된 파일과 패키지
- 사용자에게 보이는 변경사항
- 내부 구조 변경이나 문서 변경
- 실제로 실행된 테스트 또는 확인되지 않은 검증 항목

PR 본문을 작성할 때 관련 작업은 브랜치명의 `MOM-0000` 패턴을 우선 사용합니다.

프롬프트에서 Momens 티켓 번호를 직접 알려준 경우에는 프롬프트 값을 우선합니다.

브랜치명과 프롬프트 어디에도 Momens 티켓 번호가 없으면 임의로 만들지 않습니다.

테스트 결과와 스크린샷 여부도 임의로 만들지 않습니다.

## 주의할 점

- 이 에이전트들은 읽기 전용 탐색과 리뷰 목적입니다.
- 파일 수정이 필요하면 구현 작업을 별도로 요청해야 합니다.
- 리뷰 결과는 권장사항이므로, 실제 반영 여부는 변경 범위와 요구사항에 맞춰 판단합니다.
- Android/Compose 작업의 최신 기준은 `AGENTS.md`, `.agents/.skills/momens-android/`,
  `docs/compose/README.md`를 우선 확인합니다.
- 작업 관리, 브랜치, 커밋, PR 흐름의 최신 기준은 `AGENTS.md`와 `.agents/.skills/momens-workflow/`를 우선 확인합니다.
