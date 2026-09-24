---
name: clean-architecture-feature
description: |
  Add, extend, or refactor a feature in Warta following the repository's
  Clean Architecture (data/domain/presentation) and Hilt DI conventions
  (@HiltViewModel + @Module/@Binds). Use when creating a new feature module,
  wiring a ViewModel, adding a repository/usecase, or when asked to "add a
  feature", "scaffold a screen", "create a new module", or "wire a ViewModel".
tools:
  - filesystem
resources:
  - feature-template.md
---

# Clean Architecture Feature — Warta

Codified workflow for adding features. The repo follows **Package-per-Feature**
structure under `app/src/main/java/com/rhesdev/warta/feature/`. Existing
features: `home`, `detail`, `search`, `category`, `splash`.

## Principles

- **3 layers**, respecting dependency direction only inward:
  **Presentation → Domain ← Data**. Domain never depends on Android/Retrofit.
- **Hilt is the DI framework.** Use `@HiltViewModel` + `@Inject constructor`
  on ViewModels, `@Inject constructor` on repositories/usecases, and bind
  interface → impl via `@Module`/`@InstallIn(SingletonComponent::class)` +
  `@Binds`/`@Provides` in `core/di/RepositoryModule.kt`. Collect ViewModels in
  Compose with `hiltViewModel()`. Never wire dependencies manually.
- **ViewModels inject use cases, never a repository directly** — not even a
  same-feature repository, and never a different feature's repository. See
  the `mvvm-layer-boundaries` skill for the full rule.
- **Locale**: user-facing strings are **Bahasa Indonesia**; code/docs in English.
- **Offline-first architecture.** Room (`core/data/local/`) is the **Single
  Source of Truth**. UI always reads from Room via `Flow`. API updates Room,
  never bypasses directly to UI.
- **Mandatory**: Jetpack Compose with `collectAsState()` (or
  `collectAsStateWithLifecycle()`), edge-to-edge display.

## Workflow

### 1. Domain layer (`feature/<feature>/domain/`)
- **model/**: Kotlin `data class`es describing the business object(s).
- **repository/**: the **interface only** — `suspend` functions or `Flow`
  returning domain models. No implementation here.
- **usecase/**: thin orchestrators, `@Inject constructor`. Group related use
  cases into one `<Feature>UseCases.kt` file.

```kotlin
class GetTopNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<News>> = repository.getTopNews()
}
```

### 2. Data layer (`feature/<feature>/data/`)
- **repository/*RepositoryImpl.kt**: implements the domain interface.
  - Reads from Room (`Flow<List<Entity>>`) for UI observation.
  - Writes to Room after fetching from API (Retrofit).
- **remote/dto/**: API response models (`@Serializable` or Gson).
- **mapper/**: `toEntity()` and `toDomain()` extension functions.
- **local/entity/**: Room `@Entity` classes.
- **local/dao/**: Room `@Dao` interfaces.

```kotlin
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao
) : NewsRepository {

    override fun getTopNews(): Flow<List<News>> {
        return dao.getAllNews().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshNews(category: String) {
        withContext(Dispatchers.IO) {
            val response = api.getNewsByCategory(category)
            val entities = response.data.map { it.toEntity(category) }
            dao.insertAll(entities)
        }
    }
}
```

### 3. Presentation layer (`feature/<feature>/presentation/`)
- **viewmodel/XViewModel.kt**: `@HiltViewModel` + `@Inject constructor`
  (use cases only), a single `UiState` data class in a
  `MutableStateFlow` + `asStateFlow()`, `viewModelScope.launch`.

```kotlin
data class HomeUiState(
    val topNews: List<News> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { loadNews() }

    private fun loadNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTopNewsUseCase()
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { news ->
                    _uiState.update { it.copy(topNews = news, isLoading = false) }
                }
        }
    }
}
```

- **screen/XxxScreen.kt**: Material 3 composable; collect the ViewModel with
  `hiltViewModel()` and `collectAsState()`.

### 4. Wire DI (required to compile)
- **Data/use cases**: annotate with `@Inject constructor`.
- **`core/di/RepositoryModule.kt`** — add a `@Binds` entry binding the
  interface → impl.

### 5. Register navigation
- Add the route to `core/navigation/Routes.kt` as a `String` constant.
- Add a `composable(route)` entry to `core/navigation/WartaNavGraph.kt`.

### 6. Verify
```bash
sh ./gradlew :app:compileDebugKotlin
```

## Checklist
- [ ] Domain: model + repository interface + usecase(s)
- [ ] Every ViewModel injects use cases only — no `domain.repository.*` import
- [ ] Data: RepositoryImpl with `@Inject constructor`, bound via `@Binds`
- [ ] Presentation: `@HiltViewModel` (single `UiState` in `StateFlow`) + Material 3 screen
- [ ] Route added to `Routes.kt`, wired into `WartaNavGraph.kt`
- [ ] UI copy in Bahasa Indonesia
- [ ] `compileDebugKotlin` passes

## Related
- `mvvm-layer-boundaries` skill — the ViewModel → UseCase → Repository rule.
- `compose-design-system` skill — screen styling and shared components.
- `docs/NAVIGATION.md` — full navigation-graph wiring pattern.
