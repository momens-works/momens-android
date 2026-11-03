# ktlint 실행과 환경 문제 해결

이 문서는 ktlint 실행, 자동 포맷, Windows/macOS 환경 문제를 해결할 때만 읽습니다.
Compose 구현 규칙은 [Momens Compose Guide](../compose/README.md)를 따릅니다.

## 표준 명령

| 환경 | 검사 | 자동 포맷 |
| --- | --- | --- |
| macOS / Linux 터미널 | `./gradlew :app:ktlintCheck` | `./gradlew :app:ktlintFormat` |
| Windows PowerShell / 명령 프롬프트 | `.\gradlew.bat :app:ktlintCheck` | `.\gradlew.bat :app:ktlintFormat` |
| Windows Git Bash | `./gradlew :app:ktlintCheck` | `./gradlew :app:ktlintFormat` |

Gradle `ktlintCheck`가 최종 포맷 기준입니다. Android Studio의 **Reformat Code** 결과가
ktlint와 항상 같지는 않습니다.

## 실행되지 않을 때

처음 실행하는 환경에서는 Gradle Wrapper와 Java 21 toolchain을 내려받을 수 있으므로
인터넷 연결이 필요합니다. 사내·학교 네트워크, 프록시 또는 오프라인 환경에서는 다운로드가
실패해 ktlint 작업까지 도달하지 못할 수 있습니다.

다음 명령으로 stacktrace를 포함한 실패 로그를 수집합니다.

```bash
# macOS / Linux / Windows Git Bash
./gradlew :app:ktlintCheck --stacktrace

# Windows PowerShell / 명령 프롬프트
.\gradlew.bat :app:ktlintCheck --stacktrace
```

| 메시지 | 먼저 확인할 사항 |
| --- | --- |
| `JAVA_HOME`, `toolchain` | Java 21 탐색 또는 다운로드 실패 |
| `Could not resolve`, `SSL`, `proxy` | Gradle 또는 toolchain 다운로드 네트워크 문제 |
| `Access denied` | Gradle 캐시 또는 프로젝트 파일 권한 문제 |

실패 로그의 처음부터 원인 메시지까지를 공유합니다. 실행하지 않은 검사 결과를 성공으로
기록하지 않습니다.
