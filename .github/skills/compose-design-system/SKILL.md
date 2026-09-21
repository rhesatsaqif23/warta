---
name: compose-design-system
description: |
  Build or edit Jetpack Compose screens for Warta so they stay aligned with
  Material Design 3. Use when asked to "build a screen", "fix the UI",
  "style this", "make a component", or edit any composable/screen.
tools:
  - filesystem
resources: []
---

# Compose UI & Design System — Warta

Rules for writing composables that match the app's visual language. Warta uses
**Material Design 3** with Jetpack Compose.

## Where design lives
- **Theme**: `core/presentation/theme/` (`WartaTheme`, light + dark schemes).
- **Color**: `core/presentation/theme/Color.kt`.
- **Typography**: `core/presentation/theme/Type.kt`.

## Golden rules
1. **Never invent brand colors** — use Material 3 color roles
   (`MaterialTheme.colorScheme.primary/secondary/surface/error`).
2. **Use Material 3 Typography** (`MaterialTheme.typography.bodyLarge`, etc.).
3. **4dp spacing grid** — use multiples of 4 (`4, 8, 12, 16, 24, 32`).
4. **Corner radius**: cards `16dp`, buttons `12dp`, chips/badges `8dp`.
5. **Images**: use Coil's `AsyncImage` for all remote images.

## Screen building guidance

- Start from the feature's screen folder (`feature/<feature>/presentation/screen/`).
- Use `Scaffold`/`Column` with `16dp` horizontal content padding.
- Sections spaced `24dp` apart; related rows gapped `12dp`.
- Cards: `RoundedCornerShape(16dp)`, white/`surface` fill.
- Text: page titles `titleLarge`, body `bodyLarge`, labels `labelMedium`.

## Loading / Empty / Error States

Every screen that fetches data must show:
1. **Loading**: `CircularProgressIndicator` or shimmer placeholder.
2. **Empty**: informative message with optional retry action.
3. **Error**: error message with retry button.

```kotlin
@Composable
fun NewsScreen(uiState: HomeUiState) {
    when {
        uiState.isLoading -> LoadingScreen()
        uiState.error != null -> ErrorScreen(
            message = uiState.error,
            onRetry = { /* retry */ }
        )
        uiState.topNews.isEmpty() -> EmptyScreen()
        else -> NewsList(uiState.topNews)
    }
}
```

## Common Components

- **`TopBar`** — standard screen header with back chevron + title.
- **`BottomBar`** — fixed bottom navigation (Home, Kategori, Profil).
- **`NewsCard`** — reusable card for news items (thumbnail + title + date).
- **`CategoryChips`** — horizontal scrollable chip row for category filtering.

## Image Loading (Coil)

```kotlin
AsyncImage(
    model = news.imageUrl,
    contentDescription = news.title,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
)
```

## Verify
```bash
sh ./gradlew :app:compileDebugKotlin
```
