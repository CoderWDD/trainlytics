# Trainlytics 实现计划

## Phase 0: Scaffolding（脚手架）
- [ ] 更新 `gradle/libs.versions.toml`（添加Compose、Hilt、Room、Nav等依赖）
- [ ] 更新 `build.gradle.kts`（顶层：Hilt插件；app：Compose编译器、Hilt）
- [ ] 更新 `app/build.gradle.kts`（启用Compose、buildFeatures、viewBinding=false）
- [ ] 创建 `TrainlyticsApp.kt`（Application类 + @HiltAndroidApp）
- [ ] 更新 `AndroidManifest.xml`（声明Application、MainActivity）
- [ ] 创建 `MainActivity.kt`（@AndroidEntryPoint, setContent）
- [ ] 创建设计系统：`Color.kt`、`Theme.kt`、`Type.kt`
- [ ] 创建导航路由：`NavRoutes.kt`、`TrainlyticsNavGraph.kt`

## Phase A: Domain Layer（领域层）
- [ ] 创建领域实体：`BodyRecord`、`MealRecord`、`FoodItem`、`WorkoutSession`、`WorkoutSet`、`Exercise`、`WorkoutTemplate`、`MealTemplate`、`UserGoal`、`UserProfile`
- [ ] 创建Repository接口：`BodyRepository`、`MealRepository`、`WorkoutRepository`、`TemplateRepository`、`SettingsRepository`
- [ ] 创建UseCases：`GetTodaySummaryUseCase`、`RecordBodyStatsUseCase`、`AddMealRecordUseCase`、`GetMealHistoryUseCase`、`StartWorkoutSessionUseCase`、`CompleteWorkoutSessionUseCase`、`GetWeightTrendUseCase`、`GetWeeklyReviewUseCase`、`GetPersonalRecordsUseCase`

## Phase B: Data Layer（数据层）
- [ ] 创建Room实体：`BodyRecordEntity`、`MealRecordEntity`、`FoodItemEntity`、`WorkoutSessionEntity`、`WorkoutSetEntity`、`ExerciseEntity`、`WorkoutTemplateEntity`、`MealTemplateEntity`
- [ ] 创建DAOs：`BodyRecordDao`、`MealRecordDao`、`FoodItemDao`、`WorkoutDao`、`TemplateDao`
- [ ] 创建 `TrainlyticsDatabase.kt`（Room数据库）
- [ ] 创建 `UserSettingsDataStore.kt`
- [ ] 创建Repository实现
- [ ] 创建Hilt模块：`DatabaseModule`、`RepositoryModule`

## Phase C: UI Layer（UI层）
- [ ] 设计系统组件库（通用Composables）
- [ ] 今日模块：`TodayDashboardScreen`、`QuickAddSheet`、`RecordBodyStatsScreen`、`RecordMealScreen`
- [ ] 训练模块：`StartWorkoutSheet`、`ActiveWorkoutScreen`、`WorkoutSummaryScreen`
- [ ] 历史模块：`HistoryListScreen`、`HistoryDayDetailScreen`、`BackfillJournalScreen`
- [ ] 洞察模块：`InsightsDashboardScreen`、`WeeklyReviewScreen`、`WeeklyComparisonScreen`
- [ ] 模板模块：`TemplateGalleryScreen`、`WorkoutTemplateEditorScreen`、`MealTemplateEditorScreen`
- [ ] 通用组件：`ExercisePickerSheet`、`FoodSearchSheet`、`AIFoodRecognitionScreen`、`ManualFoodEntryScreen`、`PersonalRecordsScreen`
- [ ] 设置模块：`SettingsScreen`、`ProfileScreen`、`NotificationSettingsScreen`、`ExportDataScreen`、`DataImportScreen`
- [ ] 引导模块：`OnboardingScreen`、`GoalSetupScreen`
- [ ] 完成导航图连接所有页面
