# Feature Template — Warta

Scaffold reference for adding a new `<feature>` with three-layer Clean
Architecture and Hilt DI. Replace `<feature>`, `Thing`/`Things` placeholders.

Paths are relative to `app/src/main/java/com/rhesdev/warta/`.

## Domain

### `feature/<feature>/domain/model/Thing.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.domain.model

data class Thing(
    val id: String,
    val name: String,
    // ...
)
```

### `feature/<feature>/domain/repository/ThingsRepository.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThingsRepository {
    fun getThings(): Flow<List<Thing>>
    suspend fun refreshThings()
}
```

### `feature/<feature>/domain/usecase/ThingUseCases.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThingsUseCase @Inject constructor(
    private val repository: ThingsRepository
) {
    operator fun invoke(): Flow<List<Thing>> = repository.getThings()
}

class RefreshThingsUseCase @Inject constructor(
    private val repository: ThingsRepository
) {
    suspend operator fun invoke() = repository.refreshThings()
}
```

## Data

### `feature/<feature>/data/local/entity/ThingEntity.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thing_table")
data class ThingEntity(
    @PrimaryKey val id: String,
    val name: String,
    // ...
)
```

### `feature/<feature>/data/local/dao/ThingDao.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThingDao {
    @Query("SELECT * FROM thing_table")
    fun getAll(): Flow<List<ThingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ThingEntity>)
}
```

### `feature/<feature>/data/remote/dto/ThingDto.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ThingDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    // ...
)
```

### `feature/<feature>/data/mapper/ThingMapper.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.data.mapper

import com.rhesdev.warta.feature.<feature>.data.local.entity.ThingEntity
import com.rhesdev.warta.feature.<feature>.data.remote.dto.ThingDto
import com.rhesdev.warta.feature.<feature>.domain.model.Thing

fun ThingDto.toEntity() = ThingEntity(
    id = id,
    name = name
)

fun ThingEntity.toDomain() = Thing(
    id = id,
    name = name
)
```

### `feature/<feature>/data/repository/ThingsRepositoryImpl.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.data.repository

import com.rhesdev.warta.feature.<feature>.data.local.dao.ThingDao
import com.rhesdev.warta.feature.<feature>.data.mapper.toDomain
import com.rhesdev.warta.feature.<feature>.data.mapper.toEntity
import com.rhesdev.warta.feature.<feature>.data.remote.dto.ThingDto
import com.rhesdev.warta.feature.<feature>.domain.model.Thing
import com.rhesdev.warta.feature.<feature>.domain.repository.ThingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThingsRepositoryImpl @Inject constructor(
    private val dao: ThingDao,
    private val api: ThingApi
) : ThingsRepository {

    override fun getThings(): Flow<List<Thing>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshThings() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getThings()
                val entities = response.map { it.toEntity() }
                dao.insertAll(entities)
            } catch (e: Exception) {
                // Room cache remains as fallback
            }
        }
    }
}
```

## Presentation

### `feature/<feature>/presentation/viewmodel/ThingViewModel.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.<feature>.domain.model.Thing
import com.rhesdev.warta.feature.<feature>.domain.usecase.GetThingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ThingUiState(
    val things: List<Thing> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ThingViewModel @Inject constructor(
    private val getThings: GetThingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThingUiState())
    val uiState: StateFlow<ThingUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getThings()
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { things ->
                    _uiState.update { it.copy(things = things, isLoading = false) }
                }
        }
    }

    fun clearError() { _uiState.update { it.copy(error = null) } }
}
```

### `feature/<feature>/presentation/screen/ThingScreen.kt`
```kotlin
package com.rhesdev.warta.feature.<feature>.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ThingScreen(
    viewModel: ThingViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Material 3 UI with loading/empty/error states
}
```

## Navigation

### `core/navigation/Routes.kt` — add the route
```kotlin
object Routes {
    const val THING = "thing"
}
```

### `core/navigation/WartaNavGraph.kt` — add composable entry
```kotlin
composable(Routes.THING) {
    ThingScreen()
}
```

## DI Wiring

### `core/di/RepositoryModule.kt` — Hilt module
```kotlin
@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindThingsRepository(impl: ThingsRepositoryImpl): ThingsRepository
}
```

## Verify
```bash
sh ./gradlew :app:compileDebugKotlin
```
