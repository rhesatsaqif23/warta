# Product Requirements Document (PRD) — Warta App

**Dokumen ini dirancang khusus sebagai instruksi dan konteks teknis untuk AI Agent dalam proses *development* aplikasi Warta.**

---

## 1. Project Overview

* **App Name:** Warta
* **Package Name:** `com.rhesdev.warta`

* **Description:** Aplikasi agregator berita Indonesia berbasis *Offline-First* yang menyajikan berita terkini berbahasa Indonesia dalam antarmuka modern.
* **Architecture Pattern:** Clean Architecture (Data - Domain - Presentation) + MVVM.
* **UI Framework:** Jetpack Compose (100%).

---

## 2. Tech Stack Requirements

AI Agent **wajib** menggunakan pustaka berikut dengan versi stabil (atau BOM terbaru):

* **Language:** Kotlin
* **UI:** Jetpack Compose (Material Design 3)
* **Dependency Injection:** Dagger Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@HiltWorker`)
* **Local Database / Cache:** Room Database v2 (dengan dukungan Coroutines/Flow, plus 1 migrasi v1→v2)
* **Networking:** Retrofit2 + OkHttp3 (Logging Interceptor) + Gson
* **Background Work:** WorkManager (periodic refresh 30 menit, hanya saat online)
* **Asynchronous / Reactive:** Kotlin Coroutines & `StateFlow`
* **Image Loading:** Coil (`SubcomposeAsyncImage` + shimmer/error placeholder)
* **Navigation:** Jetpack Navigation Compose

---

## 3. Architecture & Folder Structure Rules

AI Agent harus mematuhi aturan ketat berikut:

1. **Package-per-Feature (satu capability = satu feature):** `feature/news/` memiliki `data/`, `domain/`, dan `presentation/` (layar `home/`, `search/`, `detail/`, `category/`). `feature/splash/` hanya berisi presentation (app entry). Modul DI dipusatkan di `core/di/`.


2. **Domain Layer Purity:** Layer `domain` tidak boleh memuat anotasi Room (`@Entity`), Retrofit (`@GET`), atau Compose. Murni Kotlin.
3. **Single Source of Truth:** UI hanya mengobservasi data dari database Room (`Flow<List<NewsEntity>>`). API bertugas meng-update Room, bukan mem-bypass langsung ke UI.
4. **State Management:** Setiap Screen harus memiliki 1 `ViewModel`, 1 `data class UiState` (loading, data, error), dan 1 `sealed interface UiEvent`; UI memanggil satu pintu `onEvent()`.
5. **UI Component:** Composable dilarang memuat *business logic*. Side effect (Intent, navigasi) di-hoist ke `Screen`; `Content` murni menerima state + callback. Pisahkan `Screen` (punya ViewModel) dan `Content` (previewable). Setiap file hanya boleh punya satu komentar `//` baris-tunggal berisi tujuan file.
6. **Dependency Direction:** `core` tidak boleh mengimpor paket `feature`. Komponen yang me-render model domain tinggal di feature.

---

## 4. Data Source Specification (Free News API)

* **Base URL:** `https://freenewsapi.ai/v1/` (gratis, tanpa API key; pengganti Berita Indo API yang sudah offline)
* **Endpoints yang Digunakan:**
* `GET /search?country=ID&lang=id&size=100&sort=date&date=48h&offset=0` (feed terkini + paging)
* `GET /search?q=<kata-kunci>&sort=relevance` (filter kategori per chip, pencarian)
* `GET /search?host=<host>` (filter penerbit — saat ini tidak dipakai UI, endpoint tersedia)
* `GET /search?from=<tgl>&to=<tgl+1>` (filter satu hari dari trend strip)
* `GET /stats` (facet `hosts`/`by_day`, total eksak; default `strict_country=true`)
* `GET /article?url=` (teks penuh satu artikel untuk Detail)


* **Response JSON Mapping:**
  Item `results` (`id`, `url`, `title`, `description`, `published_at`, `host`, `sitename`, `image`, `categories`) wajib di-mapping menjadi `NewsDto` (nullable), lalu `NewsEntity` (non-null, `link` sebagai Primary Key, `category` = kategori pertama, `source` = `sitename ?: host`), lalu model domain `News`. Respons `article` menambah field `text` menjadi kolom `content`.
  Banyak artikel memiliki `"image": null` — UI wajib memfilter tampilan ke yang bergambar dan memakai placeholder.

---

## 5. UI/UX Flow & Screen Specifications

### A. Splash Screen (`feature/splash`)

* **UI:** Logo Warta dengan teks, fade-in animation.
* **Behavior:** Otomatis navigasi ke Home setelah delay 2 detik.

### B. Home Screen (`feature/news/presentation/home`)

* **Top Bar:** Logo aplikasi (kiri), search field pill read-only (tengah, tap → Search), ikon menu (kanan).
* **Kategori Filter (Chips):** Deretan horizontal (Semua, Nasional, Teknologi, ...). Tap chip langsung update seleksi (filter lokal) dan fetch keyword kategori di background **tanpa** indikator loading.
* **Trend Strip:** Sparkline 7 batang ("Sepekan terakhir") dari facet stats; tap batang memfilter hari itu.
* **Headline:** Carousel kartu besar dengan gradient scrim; seluruh kartu clickable (tanpa tombol Baca).
* **Popular / Trending Now:** Section header + kartu dengan style shadow seragam.
* **Bottom Navigation:** Home, Kategori, Profil (ikon saja, indikator pill).
* **Pull-to-Refresh:** Swipe down memicu event `OnRefresh` yang sama; indikator hanya untuk refresh manual dan loading awal.
* **Pagination:** Footer spinner di akhir daftar (feed tanpa filter) memuat 100 artikel berikutnya.

### C. Category Screen (`feature/news/presentation/category`)

* **UI:** Daftar section per kategori (header ikon grid + label), masing-masing maksimal 3 artikel (atau kurang bila datanya segitu; section kosong dilewati).
* **Expand:** Tombol "Lainnya >>>" (warna Accent, rata kanan) membuka semua artikel section itu secara inline.
* **Top Bar + Bottom Bar:** Sama seperti Home (varian logo).

### D. Search Screen (`feature/news/presentation/search`)

* **UI:** Field pencarian pill yang konsisten dengan TopBar (radius 40, teks `bodyLarge`), autofocus, tombol clear saat ada teks.
* **Logic:** Debounce 300ms → fetch API → baca Room; subtitle jumlah eksak (`±12.400 artikel`); empty state memakai ilustrasi.
* **Top Bar:** Varian khusus (editable field + tombol back); tidak memakai `WartaTopBar` read-only.

### E. News Detail Screen (`feature/news/presentation/detail`)

* **UI:** TopBar varian back-arrow (+ search + menu-sebagai-share), gambar, kategori, judul, sumber, tanggal terformat, dan isi berita.
* **Logic:** Isi penuh dimuat malas (*lazy*) saat "Baca Selengkapnya" ditekan via `GetArticleBodyUseCase` (cache Room dulu, lalu API, lalu simpan); gagal (mis. 404) → tombol "Buka di Browser". Share memakai judul + excerpt 500 karakter + link.

### F. Exit Dialog (belum diimplementasikan)

* **Rencana:** `AlertDialog` "Mau Keluar?" saat tombol back di Home. Saat ini belum ada — jangan berasumsi sudah ada.

---

## 6. Database (Room) Schema

Agent harus memakai entitas berikut di `feature/news/data/local/NewsEntity.kt` (database v2):

```kotlin
@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey val link: String, // Tautan unik sebagai Primary Key
    val title: String,
    val contentSnippet: String,
    val isoDate: String,
    val imageUrl: String,
    val source: String,
    val category: String,
    val content: String = "" // Isi penuh, diisi malas oleh Detail (migrasi v1→v2)
)
```

Migrasi `MIGRATION_1_2` (`ALTER TABLE ... ADD COLUMN`) wajib didaftarkan di Hilt module. Singleton database manual dilarang — Hilt pemilik tunggal.

---

## 7. AI Agent Execution Plan (Step-by-Step)

Fase 1–6 (init, data, domain, design system, presentation, navigation) dan integrasi API (search/stats/article) **sudah selesai**. Untuk pekerjaan lanjutan:

* Ikuti pola yang ada: 1 UseCase = 1 tugas (`operator fun invoke`), 1 UiState + 1 UiEvent per screen, observe Room sekali di `init`, filter di derived state.
* Hapus kode mati beserta rantainya (UseCase, repo method, DAO query, endpoint) alih-alih membiarkannya.
* Perbarui `docs/` (`DATA_LAYER`, `DOMAIN_LAYER`, `PRESENTATION_LAYER`, `DATA_FLOW`, `ARCHITECTURE_SUMMARY`) setiap kali kontrak berubah.
* Jangan menyebut terminologi "Phase X" di commit message — deskripsikan perubahannya.
