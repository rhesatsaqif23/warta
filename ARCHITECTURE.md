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
- Repository **interface** didefinisikan di `domain`, implementasinya di `data`.
- UseCase: 1 use case = 1 aksi, pakai `operator fun invoke()`.

### 3. MVVM + Presentation

- ViewModel hanya berkomunikasi lewat UseCase, tidak pernah langsung ke Repository/DAO.
- State management: satu `data class UiState` per screen, di-expose via `StateFlow`.
- Event/one-time action (toast, navigasi) pakai `Channel`/`SharedFlow`, bukan state.
- Tidak ada logic bisnis di dalam Composable.

### 4. Jetpack Compose

- Stateless composable + state hoisting.
- Pemisahan `Screen` (punya ViewModel) dan `Content` (pure UI, previewable).
- Wajib ada `@Preview` untuk komponen UI utama.
- Penggunaan design system/theme yang sudah ada, dilarang hardcode color & dimension.

### 5. Dependency Injection — Hilt

- `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`.
- Penempatan module: `@Binds` untuk repository impl, `@Provides` untuk Room/Retrofit instance.
- Pemilihan scope (`@Singleton` vs `@ViewModelScoped`).

### 6. Package per Feature

Struktur modularisasi berbasis fitur, bukan berbasis tipe file.

```text
feature/
 └── product/
      ├── data/
      │    ├── local/       (dao, entity)
      │    ├── remote/      (api, dto)
      │    ├── mapper/
      │    └── repository/  (ProductRepositoryImpl)
      ├── domain/
      │    ├── model/
      │    ├── repository/  (ProductRepository - interface)
      │    └── usecase/
      ├── presentation/
      │    ├── list/        (Screen, ViewModel, UiState, components)
      │    └── detail/
      └── di/               (ProductModule)
```

### 7. Room

- Definisi `@Entity`, `@Dao`, `@Database`, dan penulisan migration.
- DAO mengembalikan `Flow` untuk data observable.
- Entity Room tidak boleh bocor ke layer `presentation` — wajib lewat mapper.

---

## ✅ Deliverable

Buat **1 sample feature sederhana end-to-end** (misal: list + detail data dummy) yang:

- Mengambil data dari API, cache ke Room, tampil di Compose.
- Menerapkan seluruh struktur dan aturan di atas.
- Push ke branch `onboarding/<nama>` lalu ajukan PR untuk direview.

---

## 📋 Acceptance Criteria

- [ ] Struktur folder mengikuti pola package-per-feature di atas
- [ ] `domain` layer bebas dari dependency Android/framework
- [ ] ViewModel tidak mengakses Repository implementation atau DAO secara langsung
- [ ] Semua dependency di-inject via Hilt, tidak ada instansiasi manual
- [ ] UI state terpusat dalam satu `UiState`, handling loading/error/success lengkap
- [ ] Ada mapper antara DTO ↔ Entity ↔ Domain Model
- [ ] Composable utama punya `@Preview` dan tidak menyimpan business logic
- [ ] PR sudah direview dan di-approve oleh tech lead

---

## 🔗 Referensi

- `<link repo / dokumentasi internal>`
- `<link feature existing yang jadi acuan>`
- `<link coding convention / lint rule>`

---

## ⏱️ Estimasi

`<3–5 hari kerja>`
