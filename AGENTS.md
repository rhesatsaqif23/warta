# AGENTS Guidelines — Warta App

This repository contains an Android application project. When working on the project interactively with an AI coding agent, please follow the guidelines below to ensure architectural consistency, maximum performance, and a smooth development experience.

## 1. Project Specifications

- **App Name:** Warta
- **Package Name:** `com.rhesdev.warta`
- **Minimum SDK:** 26 (Android 8.0)
- **Compile / Target SDK:** 37
- **Language:** Kotlin (2.2+), Java 11 (JVM target)
- **Build System:** Gradle (Kotlin DSL, AGP 9.x, KSP for Hilt/Room)

## 2. Architecture & Design Patterns

We follow **Clean Architecture (Data - Domain - Presentation) + MVVM**:

- **Presentation Layer:** `StateFlow`/`SharedFlow` in ViewModels. UI defined in Jetpack Compose (100%). Unidirectional Data Flow (UDF) via a single `onEvent()` per screen.
- **Domain Layer:** UseCases for business logic. Pure Kotlin — no Android / Retrofit / Room / Compose annotations.
- **Data Layer:** Repository pattern. Room for local (Single Source of Truth), Retrofit for remote. UI always reads from Room; the API only updates Room, never the UI directly.
- **Dependency Injection:** Dagger Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@HiltWorker`). Modules are centralized in `core/di/`.
- **Dispatchers:** Injected via qualifiers from `core/di/DispatcherModule.kt` (`@DefaultDispatcher`, `@IoDispatcher`, `@MainDispatcher`) — never hardcode `Dispatchers.IO`. See `docs/conventions/COROUTINES.md`.

## 3. Folder Structure

One capability = one feature. `news` owns its table, repository, use cases, worker, *and* every screen over them; `core` holds only what is truly shared. `feature` never imports `core/di` by hand (Hilt wires it), and `core` never imports packages from `feature`.

```
app/src/main/java/com/rhesdev/warta/
├── WartaApp.kt                    # @HiltAndroidApp + WorkManager periodic-refresh scheduling
├── core/
│   ├── di/                        # Hilt modules (DispatcherModule, NewsModule)
│   ├── domain/model/              # Shared domain models
│   ├── presentation/
│   │   ├── components/            # Design-system components (TopBar, SearchField, WartaTabHost, ...)
│   │   └── theme/                 # Material 3 theme (Color.kt, Type.kt, Theme.kt)
│   └── utils/                     # Constants (intent-extras keys), AppError, Dimens, DateFormatter
├── feature/
│   ├── news/
│   │   ├── data/
│   │   │   ├── local/             # WartaDatabase, DAO, NewsEntity
│   │   │   ├── remote/            # NewsApi + DTOs
│   │   │   ├── mapper/            # DTO ↔ Entity ↔ Domain model
│   │   │   ├── repository/        # NewsRepositoryImpl (may vary by Consumer)
│   │   │   └── work/              # NewsRefreshWorker (@HiltWorker, periodic 30 min)
│   │   ├── domain/
│   │   │   ├── model/             # News, NewsStats
│   │   │   ├── repository/        # NewsRepository (interface only)
│   │   │   └── usecase/           # 1 UseCase = 1 action (operator fun invoke)
│   │   └── presentation/          # home/ search/ detail/ category/ + shared components/
│   ├── profile/                   # Profile tab (presentation only)
│   └── splash/                    # App entry (presentation only)
└── navigation/
    └── WartaNavigator.kt          # Single navigation shield owning every Activity start
```

## 4. Asynchronous Programming

- **Concurrency:** Kotlin Coroutines exclusively. No RxJava.
- **Dispatchers:** Injected (`@IoDispatcher` etc.), never `Dispatchers.X` hardcoded in feature code. Room and Retrofit handle thread switching internally; only use an injected dispatcher for raw I/O or CPU work.
- **State Management:** `MutableStateFlow` + `asStateFlow()` in ViewModels. Single `UiState` data class + single `UiEvent` sealed interface per screen.

## 5. UI Framework

- **Jetpack Compose:** Material Design 3 by default. Split `Screen` (owns the ViewModel) and `Content` (pure, previewable). Side effects are hoisted to `Screen`.
- **Navigation:** Multi-Activity pattern — one Activity per screen, launched only through the `WartaNavigator` shield (`navigation/WartaNavigator.kt`) using Splitties typed starts (`context.start<T>{}`). No Navigation Compose. Cross-screen data travels via intent extras (≤7, keys centralized in `core/utils/Constants.kt`; JSON for heavy payloads). Tab Activities use `singleTask`. See `docs/conventions/NAVIGATION.md`.
- **Image Loading:** Coil (`AsyncImage`) with shimmer/error placeholders.
- **State Collection:** `collectAsState()` in Composables.
- **Strings & Icons:** All user-facing copy lives in `res/values/strings.xml` and is read via `stringResource(R.string.*)`; `contentDescription`s are internationalized too. Colors live in `core/presentation/theme` and dimensions in `core/utils/Dimens.kt` (no hardcoded color/dp/sp in composables; `Dimens` carries the movieApp scale plus Warta-specific sizes).

## 6. Data Flow Pattern

```
API (Retrofit) → DTO → Entity (Room) → Repository (Flow) → UseCase → ViewModel (StateFlow) → Compose UI
```

- UI always reads from Room (Single Source of Truth).
- API updates Room, never bypasses directly to UI.
- The Home feed is paginated with Paging 3: `NewsRemoteMediator` fetches API pages into Room, the repository exposes `Flow<PagingData<News>>` via a `Pager`, and the UI renders it with `collectAsLazyPagingItems()` + `cachedIn(viewModelScope)`.
- Repository is the single source of truth for data access.
- Errors are normalized once in `core/utils/AppError.kt` (`Throwable.toUserMessage()`) and reused by every ViewModel; ViewModels never output raw exception text.

## 7. Testing Philosophy

- **Unit Tests:** JUnit4, MockK for mocking, Turbine for Flow testing.
- **UI Tests:** Compose Test Rule for UI components.
- Prefer testing ViewModel state emission over testing implementation details. Injected dispatchers make it possible to swap real dispatchers for `TestDispatcher`s.

## 8. External Documentation

- [Android Developer Documentation](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org)
- [Free News API contract](docs/conventions/API_CONTRACT.md)

## 9. Useful Agent Skills

### Project-Specific (.github/skills/)
| Skill | Purpose |
|-------|---------|
| `clean-architecture-feature` | Scaffolding new features with Clean Architecture |
| `compose-design-system` | Material 3 styling and shared components |
| `mvvm-layer-boundaries` | ViewModel → UseCase → Repository rule |
| `offline-first-room` | Room persistence and offline-first strategy |
| `git-conventional-commits` | Git workflow and commit message style |
| `testing-quality` | Testing strategy and quality checks |

### General Android (.github/skills/)
| Skill Folder | Purpose |
|--------------|---------|
| `architecture/` | Clean architecture, ViewModels, Data Layer |
| `ui/` | Jetpack Compose, Coil, Accessibility |
| `performance/` | Compose and Gradle build performance |
| `concurrency_and_networking/` | Coroutines, Retrofit networking |
| `testing_and_automation/` | Unit/UI Testing, Emulator automation |
| `migration/` | XML to Compose, RxJava to Coroutines |

---

Following these practices ensures that the agent-assisted development workflow stays reliable and consistent. When in doubt, always refer to the specific agent skills provided in `.github/skills/` for deeper task-specific context, and to `docs/` for layer-by-layer detail (`DATA_LAYER.md`, `DOMAIN_LAYER.md`, `PRESENTATION_LAYER.md`, `DATA_FLOW.md`, `ARCHITECTURE_SUMMARY.md`).

*Last updated: 2026-09-25*