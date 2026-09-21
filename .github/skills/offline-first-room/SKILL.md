---
name: offline-first-room
description: |
  Create or edit Room persistence for Warta following the offline-first
  strategy. Use when building Room entities, DAOs, databases, migrations,
  DTO <-> entity mappers, or the Repository pattern, or when asked to
  "make it offline", "add a cache", "sync data", "Room", "entity", "DAO",
  or "migration".
tools:
  - filesystem
resources: []
---

# Offline-First & Room — Warta

Codified workflow for local persistence. Warta uses Room as the **Single
Source of Truth** — UI always reads from Room via `Flow`. API updates Room,
never bypasses directly to UI.

## Principles

- **UI reads Room only.** The repository writes new data to Room, and the UI
  observes Room via `Flow`. Never let the UI read the network directly.
- **DTOs are separate from `@Entity`s.** API responses are DTOs
  (`data/remote/dto/`); they are mapped to Room entities (`data/local/`) with
  `toEntity()` and back with `toDomain()`. Never expose Room entities to the
  network layer.

## Workflow

### 1. Define the local entity

```kotlin
@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey val link: String,
    val title: String,
    val contentSnippet: String,
    val isoDate: String,
    val imageUrl: String,
    val source: String,
    val category: String
)
```

### 2. DTO ↔ entity mapper

```kotlin
fun NewsDto.toEntity(category: String) = NewsEntity(
    link = link,
    title = title,
    contentSnippet = contentSnippet ?: "",
    isoDate = isoDate ?: "",
    imageUrl = image?.small ?: image?.large ?: "",
    source = "CNN",
    category = category
)

fun NewsEntity.toDomain() = News(
    link = link,
    title = title,
    contentSnippet = contentSnippet,
    isoDate = isoDate,
    imageUrl = imageUrl,
    source = source,
    category = category
)
```

### 3. DAO exposing Flow

```kotlin
@Dao
interface NewsDao {
    @Query("SELECT * FROM news_table ORDER BY isoDate DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE category = :category ORDER BY isoDate DESC")
    fun getNewsByCategory(category: String): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)
}
```

### 4. Repository

The repository exposes `Flow` for UI observation and `suspend` functions for
refreshing from API:

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
            try {
                val response = api.getNewsByCategory(category)
                val entities = response.data.map { it.toEntity(category) }
                dao.insertAll(entities)
            } catch (e: Exception) {
                // Room cache remains as fallback
            }
        }
    }
}
```

### 5. Database & migrations

- Register entities and DAOs in a `@Database` class; expose via Hilt module.
- **Never drop user data.** Any schema change = a new `Migration(oldVersion,
  newVersion)` and a bumped `version`.

```kotlin
@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class WartaDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
```

## Verify
```bash
sh ./gradlew :app:compileDebugKotlin
```

## Checklist
- [ ] DTO (`@Serializable` or Gson) separate from `@Entity`
- [ ] Mapper `toEntity()` / `toDomain()` present
- [ ] DAO returns `Flow` for observe + suspend writes
- [ ] Repository reads Room for UI, writes Room from API
- [ ] Migration added for schema changes (no data loss)
- [ ] `compileDebugKotlin` passes
