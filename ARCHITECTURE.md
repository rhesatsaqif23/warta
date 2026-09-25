# [Onboarding] Pengenalan Arsitektur & Code Style Android — Clean Architecture + MVVM (Compose, Hilt, Room)

## 🎯 Tujuan

Membekali developer baru dengan pemahaman arsitektur, struktur folder, dan konvensi penulisan kode yang dipakai di project ini, sehingga kontribusi pertama sudah konsisten dengan codebase existing.

---

## 📚 Scope Materi

### 1. Clean Architecture (3 Layer)

- Pembagian `data` → `domain` → `presentation` dan aturan dependency-nya (arah dependensi selalu menuju `domain`).
- `domain` layer harus pure Kotlin: tidak boleh import Android framework, Room, Retrofit, atau Compose.

### 2. Domain Layer

- Model/entity domain vs entity Room vs DTO response — kenapa dipisah dan cara mapping-nya.
- Repository **interface** didefinisikan di `domain`, implementasinya di `data` (`NewsRepositoryImpl`).
- UseCase: 1 use case = 1 aksi, pakai `operator fun invoke()`.

### 3. MVVM + Presentation

- ViewModel hanya berkomunikasi lewat UseCase, tidak pernah langsung ke Repository/DAO.
- State management: satu `data class UiState` per screen, di-expose via `StateFlow`.
- Event/one-time action (toast, navigasi) pakai channel/`SharedFlow`, bukan state.
- Tidak ada logic bisnis di dalam Composable.

### 4. Jetpack Compose

- Stateless composable + state hoisting.
- Pemisahan `Screen` (punya ViewModel) dan `Content` (pure UI, previewable).
- Wajib ada `@Preview` untuk komponen UI utama.
- Penggunaan design system/theme yang sudah ada, dilarang hardcode color & dimension; warna semuanya dari `core/presentation/theme` (Material 3 tokens).
- **Semua dimensi** (margin, padding, radius, elevation, tinggi kontrol, ukuran icon, ukuran gambar konten) memakai token dari `core/utils/Dimens.kt` (`Dimens.smallPadding`, `Dimens.bigRadius`, `Dimens.newsThumbWidth`, ...) — tidak ada `x.dp` hardcoded di Composable (kecuali `0.dp` sentinel).
- **Semua teks user-facing** (termasuk `contentDescription`) ditaruh di `res/values/strings.xml` dan dibaca via `stringResource(R.string.*)` — tidak ada string hardcoded di Composable.

### 5. Dependency Injection — Hilt

- `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@HiltWorker`.
- Penempatan module: `@Binds` untuk repository impl, `@Provides` untuk Room/Retrofit instance — semua terpusat di `core/di/` (`NewsModule.kt`, `DispatcherModule.kt`).
- Pemilihan scope (`@Singleton` vs `@ViewModelScoped`).
- **Dispatcher** di-inject lewat qualifier (`@DefaultDispatcher`, `@IoDispatcher`, `@MainDispatcher`) dari `DispatcherModule` — dilarang hardcode `Dispatchers.IO` di kode feature.

### 6. Package per Feature

Struktur modularisasi berbasis fitur, bukan berbasis tipe file.

```text
feature/
 ├── news/                              # satu capability: data + domain + layar + queuenya
 │    ├── data/
 │    │    ├── local/                  (WartaDatabase, NewsDao, NewsEntity)
 │    │    ├── remote/                 (NewsApi, DTOs)
 │    │    ├── mapper/                 (DTO ↔ Entity ↔ Domain)
 │    │    ├── repository/             (NewsRepositoryImpl)
 │    │    └── work/                   (NewsRefreshWorker — @HiltWorker, periodik 30 menit)
 │    ├── domain/
 │    │    ├── model/                  (News, NewsStats)
 │    │    ├── repository/             (NewsRepository - interface)
 │    │    └── usecase/                (1 use case = 1 aksi)
 │    └── presentation/
 │         ├── home/                   (Screen, ViewModel, UiState, UiEvent, components)
 │         ├── search/
 │         ├── detail/
 │         └── category/
 ├── profile/                           # tab Profil (presentation only)
 └── splash/                            # app entry, UI-only (presentation only)
```

Satu capability = satu feature: `news` memiliki tabel, repository, use case, worker,
*dan* semua layarnya — tanpa feature cangkang (`home/`, `search/` terpisah).
Modul DI dipusatkan di `core/di/` (bukan `feature/.../di/`).

### 7. Room

- Definisi `@Entity`, `@Dao`, `@Database`, dan penulisan migration.
- DAO mengembalikan `Flow` untuk data observable.
- Entity Room tidak boleh bocor ke layer `presentation` — wajib lewat mapper.

### 8. Navigation — Multi-Activity + Navigation Shield

- **Satu screen = satu Activity.** Tidak memakai Navigation Compose.
- Semua perpindahan Activity **hanya melalui** `navigation/WartaNavigator.kt` (navigation shield) memakai Splitties typed start (`context.start<T>{}`) — file lain tidak boleh memanggil `startActivity` langsung untuk Activity internal.
- Data lintas screen lewat intent extras — maksimal 7, key-nya terpusat di `core/utils/Constants.kt` (`ITEM_EXTRA_LINK`, `ITEM_EXTRA_QUERY`, `ITEM_EXTRA_CATEGORY`); payload berat dikirim sebagai satu string JSON (Gson).
- Tab (Home/Kategori/Profil) memakai `launchMode="singleTask"` + `WartaTabHost`; Detail/Search normal.
- Detail selengkapnya: `docs/conventions/NAVIGATION.md`.

---

## ✅ Deliverable

Buat **1 sample feature sederhana end-to-end** (misal: list + detail data dummy) yang:

- Mengambil data dari API, cache ke Room, tampil di Compose.
- Screens-nya di-launch lewat `WartaNavigator` (bukan `startActivity` langsung).
- Menerapkan seluruh struktur dan aturan di atas.
- Push ke branch `onboarding/<nama>` lalu ajukan PR untuk direview.

---

## 📋 Acceptance Criteria

- [ ] Struktur folder mengikuti pola package-per-feature di atas
- [ ] `domain` layer bebas dari dependency Android/framework
- [ ] ViewModel tidak mengakses Repository implementation atau DAO secara langsung
- [ ] Semua dependency di-inject via Hilt, tidak ada instansiasi manual
- [ ] Tidak ada `Dispatchers.IO` hardcoded — pakai `@IoDispatcher` yang di-inject
- [ ] Tidak ada string/`contentDescription` hardcoded — semua dari `res/values/strings.xml`
- [ ] Tidak ada `x.dp` hardcoded di Composable — pakai token `core/utils/Dimens.kt` (kecuali `0.dp` sentinel; token baru silakan tambah di `Dimens` bila Warta butuh ukuran custom)
- [ ] Error ditampilkan lewat `Throwable.toUserMessage()` (`core/utils/AppError.kt`), tidak pernah `e.message` mentah
- [ ] Semua Activity hanya dibuka lewat `WartaNavigator`; extras memakai key di `Constants.kt`
- [ ] UI state terpusat dalam satu `UiState`, handling loading/error/success lengkap
- [ ] Ada mapper antara DTO ↔ Entity ↔ Domain Model
- [ ] Composable utama punya `@Preview` dan tidak menyimpan business logic
- [ ] PR sudah direview dan di-approve oleh tech lead

---

## 🔗 Referensi

- `docs/conventions/NAVIGATION.md` — konvensi multi-Activity + extras
- `docs/conventions/COROUTINES.md` — aturan dispatcher & coroutine
- `docs/ARCHITECTURE_SUMMARY.md` — ringkasan arsitektur menyeluruh
- `docs/DATA_LAYER.md`, `docs/DOMAIN_LAYER.md`, `docs/PRESENTATION_LAYER.md`, `docs/DATA_FLOW.md`
- Acuan fitur: `feature/news/` (implementasi penuh end-to-end)

---

## ⏱️ Estimasi

`<3–5 hari kerja>`