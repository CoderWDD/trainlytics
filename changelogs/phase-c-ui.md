# Phase C: UI Layer

## Summary
Complete Jetpack Compose UI implementation for all primary screens and navigation flows.

## New Screens

### Bottom Tab Screens
- **TodayScreen** — daily overview with `WeightCard` (BMI fix using real `heightCm`), `CaloriesCard`, `WorkoutCard`
- **HistoryScreen** — 30-day calendar grid with `DayStatus` indicators (complete/partial/empty)
- **InsightsDashboardScreen** — 4-week rolling summary with per-week stats and day completion bars
- **TemplateGalleryScreen** — tabbed workout/meal template library with swipe-to-delete

### History
- **HistoryDayDetailScreen** — single-day deep dive: body stats, meals grouped by `MealType`, workout sets with PR indicator

### Templates
- **WorkoutTemplateEditorScreen** — create/edit templates with exercise picker bottom sheet (search by name/muscle group), sets/reps inputs
- **MealTemplateEditorScreen** — create/edit meal templates with inline food item form (weight, macros), running totals

### Records
- **PersonalRecordsScreen** — per-exercise best sets sorted by weight, with Epley 1RM estimate (`weight × (1 + reps/30)`)

### Settings
- **SettingsScreen** — preferences (24h clock, weight unit, week start day), nav to sub-screens
- **ProfileScreen** — name/age/height text fields + gender 3-segment selector, gradient save button
- **NotificationSettingsScreen** — channel status display + deep-link to system notification settings
- **ExportDataScreen** — per-data-type CSV export stubs (workout, meals, body, full ZIP)

### Onboarding
- **OnboardingScreen** — existing, wired to `TodayScreen` on complete/skip

### Workout
- **ActiveWorkoutScreen** — live session with set logging, timer, exercise management
- **WorkoutSummaryScreen** — post-session stats with total volume, set count, duration

## ViewModels
| ViewModel | Key Logic |
|---|---|
| `SettingsViewModel` | Observes `UserProfile` via `SettingsRepository.observeUserProfile()` |
| `HistoryDayDetailViewModel` | `SavedStateHandle["dateMillis"]`, combines 3 repo flows for single day |
| `InsightsViewModel` | 28-day load, `.chunked(7)` → 4 `WeekSummary` objects, most-recent-first |
| `TemplateGalleryViewModel` | `enum TemplateTab`, `combine()` workout+meal template flows |
| `PersonalRecordsViewModel` | Loads all exercises, fetches best set per exercise via `getBestSetForExercise()` |
| `WorkoutTemplateEditorViewModel` | `SavedStateHandle["templateId"]`, exercise search Flow, upsert on save |
| `MealTemplateEditorViewModel` | `SavedStateHandle["templateId"]`, inline item form state, upsert on save |

## Navigation
All `PlaceholderScreen` entries replaced with real screens except:
- `GoalSetup`, `BackfillJournal`, `AIFoodRecognition`, `ManualFoodEntry`, `DataImport` (retained as placeholders for future phases)

## Bug Fixes
- **WeightCard BMI** — was hardcoded to `1.75f`; now uses `state.profile.heightCm / 100f` from `UserProfile`
- **`Exercise.muscleGroup` non-nullable** — PersonalRecordsScreen uses `!= MuscleGroup.ALL` guard instead of `?.let`
- **`WorkoutSet` no `achievedAt`** — PersonalRecords sorts by `bestSet.weightKg` (not timestamp)

## Design Patterns
- Dark theme: `#0D0D0D` background, `#1A1A1A` cards, `#3FFF8B` accent green
- Gradient buttons: `Button(containerColor=Transparent)` + `Modifier.background(Brush.linearGradient(...))`
- `LaunchedEffect(state.isLoading)` to populate form fields from loaded data
- `OutlinedTextFieldDefaults.colors()` reused as `fieldColors` val in each editor screen
