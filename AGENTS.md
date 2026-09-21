# AGENTS Guidelines — Warta App

This repository contains an Android application project. When working on the project interactively with an AI coding agent, please follow the guidelines below to ensure architectural consistency, maximum performance, and a smooth development experience.

## 1. Project Specifications

- **App Name:** Warta
- **Package Name:** `com.rhesdev.warta`
- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 34
- **Language:** Kotlin (1.9+)
- **Build System:** Gradle (Kotlin DSL)

## 2. Architecture & Design Patterns

We follow **Clean Architecture (Data - Domain - Presentation) + MVVM**:

- **Presentation Layer:** `StateFlow`/`SharedFlow` in ViewModels. UI defined in Jetpack Compose (100%). Unidirectional Data Flow (UDF).
- **Domain Layer:** UseCases for business logic. Pure Kotlin — no Android/Retrofit annotations.
- **Data Layer:** Repository pattern. Room for local (Single Source of Truth), Retrofit for remote.
- **Dependency Injection:** Dagger Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`).

## 3. Folder Structure

```
app/src/main/java/com/rhesdev/warta/
├── core/
│   ├── data/
│   │   ├── local/          # Room Database, DAO, Entity
│   │   ├── remote/         # Retrofit API, DTOs
│   │   └── di/             # Hilt modules (RepositoryModule, NetworkModule)
│   ├── domain/
│   │   └── model/          # Domain models
│   └── presentation/
│       ├── components/     # Reusable UI components
│       └── theme/          # Material 3 theme (Color, Type, Theme)
├── feature/
│   ├── home/               # Home screen
│   ├── detail/             # News detail screen
│   ├── search/             # Search screen
│   ├── category/           # Category screen
│   └── splash/             # Splash screen
└── navigation/
    ├── Routes.kt           # Route definitions
    └── WartaNavGraph.kt    # NavHost configuration
```

## 4. Asynchronous Programming

- **Concurrency:** Kotlin Coroutines exclusively. No RxJava.
- **Dispatchers:** Room and Retrofit handle thread switching internally. Only specify dispatcher for raw I/O or CPU work.
- **State Management:** `MutableStateFlow` + `asStateFlow()` in ViewModels. Single `UiState` data class per screen.

## 5. UI Framework

- **Jetpack Compose:** Default for all screens. Material Design 3.
- **Navigation:** Jetpack Navigation Compose with string-based routes.
- **Image Loading:** Coil (`AsyncImage`).
- **State Collection:** `collectAsState()` in Composables.

## 6. Data Flow Pattern

```
API (Retrofit) → DTO → Entity (Room) → Repository (Flow) → UseCase → ViewModel (StateFlow) → Compose UI
```

- UI always reads from Room (Single Source of Truth).
- API updates Room, never bypasses directly to UI.
- Repository is the single source of truth for data access.

## 7. Testing Philosophy

- **Unit Tests:** JUnit4, MockK for mocking, Turbine for Flow testing.
- **UI Tests:** Compose Test Rule for UI components.
- Prefer testing ViewModel state emission over testing implementation details.

## 8. External Documentation

- [Android Developer Documentation](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org)
- [Berita Indo API](docs/API_CONTRACT.md)

## 9. Useful Agent Skills

### Project-Specific (docs/skills/)
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

Following these practices ensures that the agent-assisted development workflow stays reliable and consistent. When in doubt, always refer to the specific agent skills provided in `.github/skills/` and `docs/skills/` for deeper task-specific context.

*Last updated: 2026-09-21*
