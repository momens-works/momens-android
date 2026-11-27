# Debug Keystore 설정

Momens debug 빌드는 팀에서 공유하는 하나의 공용 debug keystore를 사용합니다.
팀원마다 새 SHA-1을 발급하거나 Google Console에 따로 등록하지 않습니다.

모든 팀원이 같은 `momens-debug.keystore`로 debug 앱을 서명하면 Google Sign-In과
Firebase debug 인증에 사용하는 SHA-1도 모두 동일해집니다.

## 1. 공용 keystore 저장

팀에서 공유받은 동일한 `momens-debug.keystore` 파일을 각자 컴퓨터에 저장합니다.
파일은 모두 같은 것이어야 하며, 각자 새로 생성하지 않습니다.

Git으로 관리되는 소스 파일과 분리된 위치에 두는 것을 권장합니다.

예시:

```text
/Users/hyem/keys/momens-debug.keystore
```

## 2. local.properties 설정

각자 로컬의 `local.properties`에 다음 값을 추가합니다. 파일 경로만 본인
컴퓨터에 저장한 위치로 바꿉니다.

```properties
debug.store.file=/Users/hyem/keys/momens-debug.keystore
debug.store.password=android
debug.key.alias=androiddebugkey
debug.key.password=android
```

`debug.store.file`에는 본인 컴퓨터에 저장한 공용 keystore의 절대 경로를 적습니다.

## 3. SHA-1 확인

다음 명령어를 실행합니다.

```bash
./gradlew signingReport
```

`debug` variant의 SHA-1이 팀에서 공유한 공용 SHA-1과 같은지 확인합니다.

Google Console 또는 Firebase에는 공용 keystore에서 나온 SHA-1 하나만 등록합니다.
팀원별 SHA-1을 따로 등록하지 않습니다.

## 4. CI 공용 keystore 설정

공유받은 동일한 `momens-debug.keystore`를 Base64로 인코딩합니다.
macOS / Linux에서는 다음 명령으로 파일을 만듭니다.

```bash
base64 < /absolute/path/momens-debug.keystore | tr -d '\n' > /tmp/momens-debug-keystore.base64
```

GitHub 저장소의 **Settings → Secrets and variables → Actions**에서
`DEBUG_KEYSTORE_BASE64` repository secret을 만들고, 생성한 파일의 내용을 저장합니다.
Base64 파일도 keystore와 마찬가지로 커밋하거나 로그에 출력하지 않습니다.

[Momens CI](../.github/workflows/pr_checker.yml)의 build job은 이 secret을
`$RUNNER_TEMP/momens-debug.keystore`로 복원하고 파일 권한을 `600`으로 설정합니다.
그 경로와 공용 비밀번호·별칭을 `local.properties`에 추가해 debug APK를 서명합니다.
secret이 없으면 기본 debug keystore로 빌드하지 않고 실패합니다.

CI에서 업로드한 APK를 내려받아 Android SDK Build Tools의 `apksigner`로 확인합니다.
`apksigner`가 PATH에 없다면 설치된 Build Tools 디렉터리의 실행 파일을 사용합니다.

```bash
apksigner verify --print-certs /absolute/path/app-debug.apk
```

출력된 인증서 SHA-1이 `signingReport`에서 확인한 공용 SHA-1과 같은지 확인합니다.
Google Cloud의 Android OAuth 클라이언트와 Firebase Android 앱에 등록한 패키지 이름은
`com.momens.android`, SHA-1은 이 공용 인증서 값이어야 합니다. 등록값이 다르면 공용
SHA-1로 맞추고 CI APK에서 Google 로그인을 확인합니다.

## 5. 필요하면 앱 삭제 후 재설치

기존 앱이 개인 debug keystore로 설치되어 있었다면, 공용 debug keystore로 서명된
앱이 업데이트 설치되지 않을 수 있습니다.

```bash
adb uninstall com.momens.android
```

삭제 후 Android Studio에서 debug 앱을 다시 실행합니다.

## 주의사항

- `momens-debug.keystore`는 커밋하지 않습니다.
- release keystore는 이 방식으로 공유하지 않습니다.
- 공용 `momens-debug.keystore`는 팀에서 공유받은 파일을 그대로 사용합니다.
- Google Console 또는 Firebase의 SHA-1 등록은 공용 keystore 기준으로 한 번만
  처리합니다.
