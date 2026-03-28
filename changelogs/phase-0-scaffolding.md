# Phase 0 – Scaffolding

Date: 2026-03-28

## Summary

Bootstrapped the project from an empty Compose template to a fully wired, buildable skeleton with all production dependencies in place.

## Changes

### Build System (`gradle/libs.versions.toml`, `build.gradle.kts`, `app/build.gradle.kts`)
- Upgraded AGP to 8.10.1, Kotlin to 2.0.21, KSP to 2.0.21-1.0.28
- Added Compose BOM 2025.05.00 (Material3, UI, Tooling, Icons Extended)
- Added Navigation-Compose 2.9.0, Lifecycle 2.9.0, Activity-Compose 1.10.1
- Added Hilt 2.56.1 + KSP processor + hilt-navigation-compose
- Added Room 2.7.1 + KSP processor
- Added DataStore Preferences 1.1.4
- Added Coroutines 1.10.2
- Enabled `buildFeatures { compose = true }` in app module
- Applied kotlin-compose, kotlin-ksp, hilt-android plugins

### Application Entry Points
- `TrainlyticsApp.kt` — `@HiltAndroidApp` Application subclass
- `MainActivity.kt` — `@AndroidEntryPoint` ComponentActivity with edge-to-edge + Compose setContent

### Design System (`core/designsystem/`)
- `Color.kt` — "Kinetic Lab" dark-first color tokens (Obsidian bg, Action Green primary, macro nutrition colors)
- `Type.kt` — Dual-typeface typography (Manrope display/headline, Inter body/label)
- `Theme.kt` — Dark-only Material3 ColorScheme + MaterialTheme wrapper

### Navigation (`core/navigation/`)
- `NavRoutes.kt` — Sealed class with all 28 app routes (incl. parameterized variants)
- `TrainlyticsNavGraph.kt` — NavHost wiring all routes to placeholder screens

### Resources
- `res/values/themes.xml` — Migrated from MaterialComponents to AppCompat DayNight NoActionBar
- `res/values-night/themes.xml` — Same migration

### AndroidManifest.xml
- Declared `android:name=".TrainlyticsApp"` Application class
- Added LAUNCHER intent-filter on MainActivity

## Build Verification

```
./gradlew :app:compileDebugKotlin  →  BUILD SUCCESSFUL
```
