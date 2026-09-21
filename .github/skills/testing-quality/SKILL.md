---
name: testing-quality
description: |
  Write and run tests for Warta. Use when writing unit tests (JUnit4),
  instrumented/integration tests, reviewing loading/empty/error states,
  or applying quality checks.
tools:
  - filesystem
resources: []
---

# Testing & Quality — Warta

Codified workflow for tests and quality checks.

## Testing strategy

- **Unit tests**: `app/src/test/java/...` (JUnit4, run on the JVM).
  - Mock `@Inject` dependencies (repository interfaces / use cases).
  - Test use cases and ViewModel state transitions.
- **Instrumented / integration tests**: `app/src/androidTest/...` (require an
  emulator).
  - Verify a screen → ViewModel → repository flow end-to-end.
  - Use Compose UI tests (`createAndroidComposeRule`) for screen-level checks.

## Quality checklist

Before calling a change done, verify:

- Navigation switches pages without getting stuck.
- Loading state appears while data loads.
- Empty state is neat and informative.
- API/connection errors displayed clearly.
- App stays safe with null data / slow connection / empty response (no crash).
- UI follows Material Design 3.

## Definition of Done

A feature is complete only when: UI matches the design system, the flow works,
data displays, loading/empty/error states exist, it never crashes, and it can
be demonstrated.

## Commands

```bash
sh ./gradlew :app:compileDebugKotlin     # after every code change
sh ./gradlew :app:testDebugUnitTest      # local JVM unit tests
sh ./gradlew :app:lintDebug              # Android lint
sh ./gradlew :app:assembleDebug          # full debug build
```

## Checklist
- [ ] Happy path + important branches covered
- [ ] Unit-tested: use cases, mappers, ViewModel state
- [ ] Loading / empty / error states on every screen
- [ ] Layers decoupled (modifiability); no presentation/data leak in domain
- [ ] `testDebugUnitTest` + `lintDebug` pass
