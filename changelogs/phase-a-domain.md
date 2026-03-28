# Phase A – Domain Layer

Date: 2026-03-28

## Summary

Implemented the complete domain layer: 13 pure-Kotlin model classes, 5 repository interfaces, and 9 use cases — zero Android framework dependencies.

## Domain Models (`domain/model/`)

| File | Description |
|------|-------------|
| `BodyRecord` | Weight / body-fat / waist measurements with timestamp |
| `FoodItem` | Food database entry with per-100g macros + category |
| `MealRecord` | Single food consumed (meal type, grams, full macro breakdown) |
| `Exercise` | Exercise definition with muscle group |
| `WorkoutSet` | Single set: exercise, weight, reps, RPE, PR flag |
| `WorkoutSession` | Training session aggregate with sets + computed volume |
| `WorkoutTemplate` | Reusable workout template with `TemplateExercise` items |
| `MealTemplate` + `MealTemplateItem` | Reusable meal template with computed macro totals |
| `UserGoal` | Target weight, calorie/macro targets, training phase |
| `UserProfile` | Personal data, units preferences, onboarding flag |
| `TodaySummary` | Single-day aggregate for the Today Dashboard |
| `DaySummary` + `DayStatus` | Per-day completeness for History screen |
| `PersonalRecord` + `WeeklyReview` | Analytics aggregates (`Analytics.kt`) |

## Repository Interfaces (`domain/repository/`)

- `BodyRepository` — CRUD + range observation for body records
- `MealRepository` — CRUD for meal records + food item search
- `WorkoutRepository` — Session lifecycle, sets CRUD, exercise search, PR queries
- `TemplateRepository` — Workout + meal template CRUD with usage counter
- `SettingsRepository` — UserProfile + UserGoal via DataStore/Room

## Use Cases (`domain/usecase/`)

| Use Case | Description |
|----------|-------------|
| `RecordBodyStatsUseCase` | Validate + persist a body measurement |
| `GetWeightTrendUseCase` | Flow of body records over a date range |
| `AddMealRecordUseCase` | Validate + persist a meal log entry |
| `GetMealHistoryUseCase` | Flow of meal records over a date range |
| `StartWorkoutSessionUseCase` | Start blank or template-based session |
| `CompleteWorkoutSessionUseCase` | Stamp finish time, return full session |
| `GetPersonalRecordsUseCase` | Calculate 1RM (Epley) for all exercises |
| `GetTodaySummaryUseCase` | Combines 4 flows → TodaySummary |
| `GetWeeklyReviewUseCase` | Compute weekly score, averages, day summaries |

## Build Verification

```
./gradlew :app:compileDebugKotlin  →  BUILD SUCCESSFUL
```
