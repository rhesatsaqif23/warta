---
name: mvvm-layer-boundaries
description: |
  Enforce the ViewModel → UseCase → Repository layering rule in Warta's
  MVVM + Clean Architecture, especially the cross-feature case. Use when
  adding a ViewModel dependency, reviewing a diff that injects a
  `domain.repository.*` type into a ViewModel, or when asked to "add a use
  case", "fix layering", or "review architecture boundaries".
---

# MVVM Layer Boundaries — Warta

A narrower, enforcement-focused companion to the `clean-architecture-feature`
skill.

## The rule

**A ViewModel never injects a `domain.repository.*` interface directly.**
It injects `domain.usecase.*` classes only — even for a single-line
pass-through. This holds for the ViewModel's *own* feature and, without
exception, for every *other* feature it needs something from.

```kotlin
// ❌ Wrong — HomeViewModel reaching into search's repository directly
class HomeViewModel @Inject constructor(
    private val getTopNews: GetTopNewsUseCase,
    private val searchRepository: SearchRepository   // <- cross-feature leak
) : ViewModel()

// ✅ Right — goes through a UseCase owned by the feature
class HomeViewModel @Inject constructor(
    private val getTopNews: GetTopNewsUseCase,
    private val searchNews: SearchNewsUseCase
) : ViewModel()
```

## How to find and fix a violation

1. Grep ViewModels for repository imports:
   ```bash
   grep -rn "domain\.repository\." app/src/main/java/com/rhesdev/warta/features/*/presentation/viewmodel/*.kt
   ```
2. Check whether a matching UseCase exists in the *owning* feature's
   `domain/usecase/`. If it does, inject that instead.
3. If no matching UseCase exists, add one in the owning feature:
   ```kotlin
   class SearchNewsUseCase @Inject constructor(
       private val repository: SearchRepository
   ) {
       operator fun invoke(query: String): Flow<List<News>> =
           repository.searchNews(query)
   }
   ```
4. Swap the ViewModel's constructor param and every call site, then run
   `./gradlew :app:compileDebugKotlin`.

## Quick checklist

- [ ] No ViewModel constructor param has type `domain.repository.*` from a
      **different** feature — inject that feature's UseCase instead.
- [ ] No ViewModel constructor param has type `domain.repository.*` from its
      **own** feature either.
- [ ] New use cases follow the touched feature's file-grouping convention.
- [ ] `./gradlew :app:compileDebugKotlin` passes after the swap.

## Related

- `clean-architecture-feature` skill — scaffolding a brand-new feature end to end.
