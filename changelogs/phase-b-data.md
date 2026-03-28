# Phase B: Data Layer

**Date:** 2026-03-28
**Branch:** develop_claude_code

## Summary

Implemented the complete data layer: Room database, all DAOs, DataStore, entity mappers, repository implementations, and Hilt DI modules. Seeded a built-in exercise library on first launch.

---

## Files Created

### Room Entities (`data/local/db/`)
| File | Entity | Table |
|------|--------|-------|
| `BodyRecordEntity.kt` | `BodyRecordEntity` | `body_records` |
| `FoodItemEntity.kt` | `FoodItemEntity` | `food_items` |
| `MealRecordEntity.kt` | `MealRecordEntity` | `meal_records` (FK → food_items SET_NULL) |
| `ExerciseEntity.kt` | `ExerciseEntity` | `exercises` |
| `WorkoutSessionEntity.kt` | `WorkoutSessionEntity` | `workout_sessions` |
| `WorkoutSetEntity.kt` | `WorkoutSetEntity` | `workout_sets` (FK → session CASCADE, FK → exercise RESTRICT) |
| `WorkoutTemplateEntity.kt` | `WorkoutTemplateEntity` | `workout_templates` |
| `TemplateExerciseEntity.kt` | `TemplateExerciseEntity` | `template_exercises` (FK → template CASCADE, FK → exercise RESTRICT) |
| `MealTemplateEntity.kt` | `MealTemplateEntity` | `meal_templates` |
| `MealTemplateItemEntity.kt` | `MealTemplateItemEntity` | `meal_template_items` (FK → template CASCADE) |
| `UserGoalEntity.kt` | `UserGoalEntity` | `user_goals` |

### DAOs (`data/local/db/`)
| File | DAOs |
|------|------|
| `BodyRecordDao.kt` | `BodyRecordDao` — upsert, deleteById, observeForDay, observeInRange, getLatest |
| `MealDao.kt` | `MealRecordDao`, `FoodItemDao` |
| `WorkoutDao.kt` | `ExerciseDao`, `WorkoutSessionDao`, `WorkoutSetDao` (Epley 1RM sort in `getBestSet`) |
| `TemplateDao.kt` | `WorkoutTemplateDao`, `TemplateExerciseDao`, `MealTemplateDao`, `MealTemplateItemDao`, `UserGoalDao` |

### Database
- `TrainlyticsDatabase.kt` — Room v1 with all 11 entities and 11 DAO accessors

### DataStore
- `UserSettingsDataStore.kt` — `@Singleton`, Preferences DataStore for `UserProfile` fields

### Entity Mappers
- `EntityMappers.kt` — bidirectional `toDomain()` / `toEntity()` extensions for all 11 entity types; safe enum parsing via `runCatching`

### Repository Implementations (`data/repository/`)
| File | Interface |
|------|-----------|
| `BodyRepositoryImpl.kt` | `BodyRepository` |
| `MealRepositoryImpl.kt` | `MealRepository` |
| `WorkoutRepositoryImpl.kt` | `WorkoutRepository` |
| `TemplateRepositoryImpl.kt` | `TemplateRepository` |
| `SettingsRepositoryImpl.kt` | `SettingsRepository` |

### Hilt DI Modules (`data/di/`)
| File | Purpose |
|------|---------|
| `DatabaseModule.kt` | `@Singleton` `TrainlyticsDatabase` + all 11 DAO `@Provides` |
| `RepositoryModule.kt` | `@Binds @Singleton` for all 5 repository interfaces |
| `ExerciseSeeder.kt` | Seeds 49 built-in exercises across 6 muscle groups on first launch |

### Application
- `TrainlyticsApp.kt` — injected `ExerciseSeeder`, calls `seed()` in `onCreate()`

---

## Key Design Decisions

- **Day bucket**: `(epochMillis / 86_400_000L) * 86_400_000L` — consistent UTC day start across all repositories
- **Template upsert atomicity**: upsert header → resolve real ID (id == 0L guard) → delete old children → re-insert all
- **Epley 1RM sort**: `ORDER BY (weightKg * (1 + reps / 30.0)) DESC` in `getBestSet` SQL query
- **Exercise seed idempotency**: `@Insert(onConflict = IGNORE)` — safe to call on every launch
- **`fallbackToDestructiveMigration(dropAllTables = true)`**: development convenience; will be replaced by proper migrations before release
- **`@Binds` vs `@Provides`**: repository bindings use abstract `@Binds` (zero runtime overhead); database uses object `@Provides`

---

## Build Verification

```
./gradlew :app:compileDebugKotlin → BUILD SUCCESSFUL (0 warnings)
```
