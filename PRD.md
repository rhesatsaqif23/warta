# Product Requirements Document (PRD) — Warta App

**Dokumen ini dirancang khusus sebagai instruksi dan konteks teknis untuk AI Agent dalam proses *development* aplikasi Warta.**

---

## 1. Project Overview

* **App Name:** Warta
* **Package Name:** `com.rhesdev.warta`

* **Description:** Aplikasi agregator berita Indonesia berbasis *Offline-First* yang menyatukan informasi dari berbagai portal media (CNN, CNBC, Tribun, dll) ke dalam satu antarmuka modern.
* **Architecture Pattern:** Clean Architecture (Data - Domain - Presentation) + MVVM.
* **UI Framework:** Jetpack Compose (100%).

---

## 2. Tech Stack Requirements

AI Agent **wajib** menggunakan pustaka berikut dengan versi stabil (atau BOM terbaru):

* **Language:** Kotlin
* **UI:** Jetpack Compose (Material Design 3)
* **Dependency Injection:** Dagger Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`)
* **Local Database / Cache:** Room Database (dengan dukungan Coroutines/Flow)
* **Networking:** Retrofit2 + OkHttp3 (Logging Interceptor) + Kotlinx Serialization / Gson
* **Asynchronous / Reactive:** Kotlin Coroutines & `StateFlow` / `SharedFlow`
* **Image Loading:** Coil (`AsyncImage`)
* **Navigation:** Jetpack Navigation Compose

---

## 3. Architecture & Folder Structure Rules

AI Agent harus mematuhi aturan ketat berikut:

1. **Package-per-Feature:** Struktur direktori wajib mengikuti pembagian fitur (misal: `feature/home`, `feature/search`), bukan berdasarkan tipe file.


2. **Domain Layer Purity:** Layer `domain` tidak boleh memuat anotasi Room (`@Entity`), Retrofit (`@GET`), atau Compose. Murni Kotlin.
3. **Single Source of Truth:** UI hanya mengobservasi data dari database Room (`Flow<List<NewsEntity>>`). API bertugas meng-update Room, bukan mem-bypass langsung ke UI.
4. **State Management:** Setiap Screen harus memiliki 1 `ViewModel` dan 1 `data class UiState` (mengandung loading, data, error).
5. **UI Component:** Composable dilarang memuat *business logic*. Semua interaksi dikirim ke `ViewModel` via event.

---

## 4. Data Source Specification (Berita Indo API)

* **Base URL:** `[https://berita-indo-api.vercel.app/v1/](https://berita-indo-api.vercel.app/v1/)`
* **Endpoints Utama yang Digunakan:**
* `GET /cnn-news/nasional` (Top Headline)
* `GET /tribun-news/teknologi` (Kategori Spesifik)
* `GET /cnn-news/` (Semua Berita)


* **Response JSON Mapping:**
  Data dari JSON `data.posts` wajib di-mapping menjadi `NewsDto`, lalu diubah menjadi `NewsEntity` untuk disimpan ke Room.
  Atribut utama: `title`, `link`, `contentSnippet`, `isoDate`, `image.small`/`image.large`.

---

## 5. UI/UX Flow & Screen Specifications

Referensi desain UI terdapat pada gambar purwarupa Warta. AI Agent harus merender layar-layar berikut menggunakan Jetpack Compose:

### A. Splash Screen (`feature/splash`)

* **UI:** Menampilkan logo huruf "W" disusul teks "Warta", dan tombol "Akses Warta" di bagian bawah layar pada versi terakhir.


* **Behavior:** Menggunakan delay coroutine singkat (contoh: 2 detik) atau *event listener* pada tombol untuk bernavigasi ke Home.

### B. Home Screen (`feature/news/presentation/home`)

* **Top Bar:** Memuat logo aplikasi di kiri dan ikon *Search* di kanan.


* **Kategori Filter (Chips):** Deretan horizontal *chips* (Semua, Kesehatan, Olahraga, Bisnis, dll). Jika chip diklik, ViewModel men-trigger pengambilan data berdasarkan parameter kategori.


* **Headline (Berita Utama):** Kartu berita besar di atas dengan gambar penuh dan judul di-overlay *gradient*.


* **Popular / Trending Now:** *LazyColumn* atau daftar vertikal untuk daftar berita lainnya, menampilkan *thumbnail* (kiri) dan judul beserta tanggal (kanan).


* **Bottom Navigation:** Menampilkan menu navigasi (Home, Kategori, Profil).



### C. Category & Sidebar Screen (`feature/news/presentation/category`)

* **UI:** Menampilkan daftar kategori secara vertikal atau grid.


* **Sidebar:** Terdapat *drawer* atau halaman khusus yang mengelompokkan berita berdasarkan portal (misal: CNN, FIFA/Top News).



### D. Search Screen (`feature/search`)

* **UI:** *TextField* pencarian di atas, deretan *chips* riwayat pencarian (atau filter pencarian), dan daftar *LazyColumn* untuk hasil pencarian.


* **Logic:** Terapkan *debouncing* (misal: 300ms) pada input text via `StateFlow` sebelum memanggil `SearchNewsUseCase`.

### E. News Detail Screen (`feature/news/presentation/detail`)

* **UI:** Layar *Berita 1* menampilkan gambar ukuran penuh di bagian atas, judul besar, kategori (misal: "OLAHRAGA"), meta informasi (tanggal), teks deskripsi, dan tombol aksi (Share).


* **Logic:** Karena API hanya memberi *snippet*, tambahkan *Floating Action Button* atau link teks "Baca Selengkapnya" yang memicu `Intent.ACTION_VIEW` untuk membuka `link` asli berita di *browser*.

### F. Exit Dialog (`core/presentation/components`)

* **UI:** Sebuah `AlertDialog` di Compose bertuliskan "Mau Keluar?" dengan tombol "Batal" dan "Keluar".


* **Logic:** Ditampilkan ketika user menekan tombol *back* (menggunakan `BackHandler`) di Home Screen. Jika user memilih "Keluar", aplikasi ditutup dan menampilkan layar perpisahan ("Terima kasih telah menggunakan Warta").



---

## 6. Database (Room) Schema

Agent harus membuat entitas berikut di `core/data/local/NewsEntity.kt`:

```kotlin
@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey val link: String, // Tautan unik sebagai Primary Key
    val title: String,
    val contentSnippet: String,
    val isoDate: String,
    val imageUrl: String,
    val source: String, // Misal: "CNN", "Tribun"
    val category: String // Untuk keperluan filtering di Room
)

```

---

## 7. AI Agent Execution Plan (Step-by-Step)

Agent harus mengeksekusi pembangunan aplikasi dalam urutan berikut dan melakukan *commit* per fase:

* **Phase 1: Project Initialization:** Konfigurasi `build.gradle.kts` untuk Hilt, Compose, Room, dan Retrofit. Buat class `WartaApp` (`@HiltAndroidApp`) dan *setup* `MainActivity.kt` (`@AndroidEntryPoint`) di `com.rhesdev.warta`.


* **Phase 2: Core & Data Layer:** Buat `WartaDatabase`, DAO, `RetrofitClient`, dan `NewsApi`. Implementasikan mapper DTO ke Entity dan Entity ke Domain.
* **Phase 3: Domain Layer:** Buat `News` model, `NewsRepository` interface, dan UseCases (`GetTopNewsUseCase`, `GetNewsByCategoryUseCase`).
* **Phase 4: Design System & Components:** Pindahkan konfigurasi bawaan `ui.theme` ke `core/presentation/theme/`. Buat komponen *reusable* (`NewsCard`, `TopBar`, `BottomNavigationBar`).


* **Phase 5: Presentation & ViewModels:** Bangun `UiState`, `ViewModel`, dan layar-layar Compose (Splash, Home, Detail, Search) persis mengikuti struktur gambar UI.


* **Phase 6: Navigation & Tying it Together:** Terapkan `NavHost` di `MainActivity`, hubungkan semua layar, jalankan tes integrasi aliran data (API -> Room -> UI).