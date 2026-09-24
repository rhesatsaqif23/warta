# Warta

Aplikasi agregator berita Indonesia berbasis *Offline-First* yang menyajikan berita terkini berbahasa Indonesia dalam antarmuka modern.

## Features

- **Latest News Feed**: Indonesian headlines refreshed from the API (48h window, 100 per page)
- **Offline-First**: Read news even without internet connection (Room cache)
- **Category Filtering**: Chips (Semua, Nasional, Teknologi, Ekonomi, ...) fetch per-category articles by keyword
- **Trend Strip**: 7-day coverage sparkline from API stats; tap a bar to filter that day
- **Search**: Debounced search with exact result counts and match illustrations
- **In-App Full Text**: Article bodies load inside Detail with offline caching
- **Pull-to-Refresh**: Swipe down on Home and Category feeds
- **Pagination**: Endless scroll appends older articles
- **Background Refresh**: Periodic update every 30 minutes when online
- **Dark Mode**: System-wide dark theme support
- **Shimmer Loading**: Skeleton placeholders while images load

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Dagger Hilt |
| Local DB | Room Database (v2, with migration) |
| Network | Retrofit + OkHttp |
| Background Work | WorkManager + Hilt |
| Image Loading | Coil (+ shimmer/error placeholders) |
| Navigation | Jetpack Navigation Compose |

## Project Structure

```
app/src/main/java/com/rhesdev/warta/
├── core/
│   ├── di/                   # Hilt modules (network, database, repository)
│   └── presentation/         # Theme + generic design-system components
├── feature/
│   ├── news/
│   │   ├── data/             # Room, Retrofit DTOs, mappers, repository impl, worker
│   │   ├── domain/           # Pure-Kotlin models, repository contract, use cases
│   │   └── presentation/     # home/ search/ detail/ category/ screens
│   └── splash/               # Splash screen
└── navigation/               # Routes, NavGraph (Scaffold + bottom bar)
```

One capability = one feature: `news` owns its table, repository, use cases,
and every screen over them. See `ARCHITECTURE.md` and `docs/ARCHITECTURE_SUMMARY.md`.

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or device (min SDK 26, target SDK 37)

No API key needed.

## API

Uses [Free News API](https://freenewsapi.ai) (`/v1/search`, `/v1/stats`, `/v1/article`)
for Indonesian news — free, keyless. (Previously Berita Indo API, now offline.)

## License

MIT
