# Warta

Aplikasi agregator berita Indonesia berbasis *Offline-First* yang menyatukan informasi dari berbagai portal media (CNN, CNBC, Tribun, dll) ke dalam satu antarmuka modern.

## Features

- **Multi-Source News**: Aggregates news from CNN, CNBC, Tribun, Antara, and more
- **Offline-First**: Read news even without internet connection
- **Category Filtering**: Filter by nasional, ekonomi, olahraga, teknologi, etc.
- **Search**: Search news by title with debounced input
- **Dark Mode**: System-wide dark theme support

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Dagger Hilt |
| Local DB | Room Database |
| Network | Retrofit + OkHttp |
| Image Loading | Coil |
| Navigation | Jetpack Navigation Compose |

## Project Structure

```
app/src/main/java/com/rhesdev/warta/
├── core/
│   ├── data/           # Room, Retrofit, Mappers
│   ├── di/             # Hilt Modules
│   ├── domain/         # Models, Repository Interfaces, UseCases
│   └── presentation/   # Theme, Reusable Components
├── feature/
│   ├── splash/         # Splash Screen
│   ├── home/           # Home Screen
│   ├── detail/         # News Detail Screen
│   └── search/         # Search Screen
└── navigation/         # Routes, NavGraph
```

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or device (min SDK 24)

## API

Uses [Berita Indo API](https://github.com/satyawikananda/berita-indo-api) for news data.

## License

MIT
