# Momens AI Agent Guide

Momens is an Android app built with Kotlin, Jetpack Compose, MVVM, StateFlow,
and Hilt. Code style is enforced with ktlint and `.editorconfig`.

## Read First

Choose the smallest set of guides that matches the work.

| Work | Required guide |
| --- | --- |
| Android, Kotlin, Compose, ViewModel, UI review | `.agents/.skills/momens-android/SKILL.md` |
| Task, branch, commit, PR, or GitHub migration | `.agents/.skills/momens-workflow/SKILL.md` |
| Compose implementation details | `.agents/.skills/momens-android/references/compose-basic-flow.md` and `.agents/.skills/momens-android/references/momens-project-flow.md` |
| Project-specific Compose workflow | `docs/compose/README.md` |
| ktlint, formatting, or Windows/macOS environment issues | `docs/development/ktlint-environment.md` |

Codex must use the applicable Momens skill. Other AI tools must read the same
files directly.

## Rule Precedence

Apply instructions in this order:

1. The user's current request
2. This file's safety, scope, and validation rules
3. The applicable Momens skill and its required references
4. Existing code style in the relevant feature

If two project instructions conflict and the order above does not resolve it,
stop and ask before changing code.

## Before Editing

- Inspect the relevant feature package and existing implementation style.
- Run `git status`; preserve unrelated uncommitted work. Do not modify, stage,
  discard, or revert it.
- Keep changes within the requested scope. Avoid unrelated refactors.

## Do Not

- Do not run destructive Git commands, force-push, delete branches, or discard
  changes unless the user explicitly requests it.
- Do not claim a build, test, lint check, task update, commit, or PR succeeded
  unless it actually ran and succeeded.
- Do not invent Momens task labels, assignees, PR URLs, issue links, or test
  results.
- Do not expose API response models directly to UI code.
- Do not create a new design-system component before checking whether an
  existing component meets the need.

## Android and Compose Rules

- Keep screen state and business logic in the ViewModel.
- Keep Composables focused on rendering state; send events upward through lambdas.
- Follow Route / Screen / Component responsibilities. Reusable components must
  not reference a ViewModel or NavController.
- Prefer existing design-system components and `MomensTheme`.
- Add or update a Preview for a new Composable or meaningful UI state. If that
  is impractical, explain why in the PR.

## Validation

- Kotlin or Compose changes: run the platform-appropriate Gradle Wrapper
  command for `:app:ktlintCheck`.
- UI state, ViewModel, navigation, build, or other compile-sensitive changes:
  also run the platform-appropriate Gradle Wrapper command for
  `:app:compileDebugKotlin`.
- Run the smallest relevant test set when tests exist.
- If no relevant automated test exists, state that and record the validation
  that did run in the PR.
- Use the platform-appropriate Gradle Wrapper command from
  `docs/development/ktlint-environment.md`; it contains the exact macOS, Linux, and
  Windows commands. Gradle `ktlintCheck` is the final formatting authority,
  not IDE reformatting.

## Momens and GitHub Workflow

Momens is the source of truth for work tracking. Create and reference Momens
tasks (`MOM-0000`) instead of GitHub issues for normal work.

```text
branch: type/MOM-0000-short-summary
commit: [TYPE/MOM-0000] Summary
PR:     [TYPE/MOM-0000] Summary
```

`TYPE` in commits and PR titles must be uppercase. Use the PR template and
record only validation that actually ran.

## Repository Landmarks

```text
app/src/main/java/com/momens/android/
├── core/          # shared infrastructure and design system
├── data/          # remote, local, repository implementations
└── presentation/  # Compose features, ViewModels, UI state

.agents/.skills/      # Momens Android and workflow guides
docs/compose/         # Compose architecture, UI, and feature workflows
docs/development/     # development-environment troubleshooting guides
```
