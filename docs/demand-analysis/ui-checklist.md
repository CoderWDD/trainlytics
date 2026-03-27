# Trainlytics UI 页面与组件清单

> 完整的页面清单、组件库和导航结构说明

---

## 一、页面总览

### 底部导航结构（4 个主 Tab）

```
今日 (Today) | 历史 (History) | 洞察 (Insights) | 模板 (Templates)
```

### 页面统计

| 类型 | 数量 |
|------|------|
| Tab 主页面 | 4 |
| 子页面（全屏） | 20 |
| 底部弹窗（Bottom Sheet） | 4 |
| **合计** | **28** |

---

## 二、完整页面清单

### 模块零：引导/启动（Launch）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 1 | `onboarding` | 引导欢迎页 | 全屏（首次启动） | 应用首次启动 | goal_setup |
| 2 | `goal_setup` | 目标设置 | 全屏（首次/重设） | onboarding, profile「重新设置目标」 | today_dashboard（首次）/ profile（重设） |

### 模块一：今日（Today）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 3 | `today_dashboard` | 今日总览 | Tab 主页 | 底部导航「今日」/ 训练完成返回 | quick_add_sheet, active_workout, record_body_stats, record_meal_sheet, settings |
| 4 | `quick_add_sheet` | 快速添加 | 底部弹窗 | today_dashboard FAB「+」 | record_body_stats, record_meal_sheet, start_workout_sheet |
| 5 | `record_body_stats` | 记录身体数据 | 子页面 | today_dashboard, quick_add_sheet, backfill_journal | 保存返回上页 |
| 6 | `record_meal_sheet` | 餐食记录 | 子页面 | quick_add_sheet, today_dashboard, backfill_journal | ai_food_recognition, manual_food_entry, 保存返回 |
| 7 | `start_workout_sheet` | 开始训练 | 底部弹窗 | quick_add_sheet, backfill_journal | active_workout, workout_template_editor |
| 8 | `active_workout` | 训练进行中 | 全屏子页面 | today_dashboard「继续训练」, start_workout_sheet | exercise_picker, workout_summary |

### 模块二：训练完成（Workout Completion）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 9 | `workout_summary` | 训练完成总结 | 全屏子页面 | active_workout「完成训练」 | personal_records, today_dashboard |

### 模块三：历史（History）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 10 | `history_list` | 历史记录 | Tab 主页 | 底部导航「历史」 | history_day_detail, backfill_journal |
| 11 | `history_day_detail` | 单日详情 | 子页面 | history_list 点击日期卡 | 返回 history_list |
| 12 | `backfill_journal` | 补记日志 | 子页面 | history_list「补记日志」 | record_body_stats, record_meal_sheet, start_workout_sheet |

### 模块四：洞察（Insights）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 13 | `insights_dashboard` | 数据分析 | Tab 主页 | 底部导航「洞察」 | weekly_review, weekly_comparison |
| 14 | `weekly_review` | 每周回顾 | 子页面 | insights_dashboard | 返回 insights_dashboard |
| 15 | `weekly_comparison` | 多周对比 | 子页面 | insights_dashboard | 返回 insights_dashboard |

### 模块五：模板（Templates）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 16 | `template_gallery` | 模板管理 | Tab 主页 | 底部导航「模板」 | workout_template_editor, meal_template_editor |
| 17 | `workout_template_editor` | 训练模板编辑 | 子页面 | template_gallery, start_workout_sheet「创建新模板」 | exercise_picker, 保存→template_gallery |
| 18 | `meal_template_editor` | 餐食模板编辑 | 子页面 | template_gallery 餐食模板卡 | food_search_sheet, 保存→template_gallery |

### 模块六：通用组件（Shared）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 19 | `exercise_picker` | 选择动作 | 底部弹窗 | active_workout, workout_template_editor | 返回并添加所选动作 |
| 20 | `food_search_sheet` | 食物搜索 | 底部弹窗 | meal_template_editor | manual_food_entry, ai_food_recognition, 添加后返回 |
| 21 | `ai_food_recognition` | AI食物识别 | 子页面 | record_meal_sheet, food_search_sheet | 确认返回上页 |
| 22 | `manual_food_entry` | 手动食物录入 | 子页面 | record_meal_sheet, food_search_sheet | 保存返回上页 |
| 23 | `personal_records` | 个人记录（PR） | 子页面 | workout_summary | 返回 / today_dashboard |

### 模块七：设置/账户（Settings & Account）

| # | 页面ID | 中文名 | 类型 | 入口来源 | 出口目标 |
|---|--------|--------|------|----------|----------|
| 24 | `settings` | 设置 | 独立页面 | today_dashboard 头像入口 | profile, notification_settings, export_data, data_import |
| 25 | `profile` | 个人资料 | 子页面 | settings | goal_setup（重设目标） |
| 26 | `notification_settings` | 通知设置 | 子页面 | settings | 返回 settings |
| 27 | `export_data` | 导出数据 | 子页面 | settings | 触发导出, 返回 settings |
| 28 | `data_import` | 数据导入 | 子页面 | settings | 完成返回 settings |

---

## 三、组件清单

### 3.1 导航组件

| 组件名 | 描述 | 使用页面 |
|--------|------|----------|
| `BottomNavBar` | 毛玻璃效果底部导航栏（4 Tab） | 所有 Tab 主页 |
| `TopAppBar` | 顶部应用栏（标题 + 操作按钮） | 所有子页面 |
| `BackButton` | 返回箭头按钮 | 所有子页面 |
| `FAB` | 浮动操作按钮（「+」图标） | today_dashboard, template_gallery |

### 3.2 数据展示组件

| 组件名 | 描述 | 使用页面 |
|--------|------|----------|
| `BentoGrid` | Bento 网格布局容器 | today_dashboard, weekly_review, history_day_detail, ai_food_recognition |
| `MacroCard` | 宏量四象限卡（卡路里/蛋白质/碳水/脂肪） | record_meal_sheet, manual_food_entry, meal_template_editor, ai_food_recognition |
| `WeightTrendChart` | 体重趋势折线图（含移动平均线） | insights_dashboard |
| `CalorieBarChart` | 热量摄入柱状图 | insights_dashboard |
| `ProteinDonutChart` | 蛋白质达成率环形图 | insights_dashboard |
| `StrengthProgressCard` | 力量进展卡（1RM 估算） | insights_dashboard, weekly_review |
| `MacroProgressBar` | 宏量目标进度条 | history_day_detail |
| `WeeklyCalendarStrip` | 周日历条（颜色编码圆点） | history_list |
| `DailyActivityCard` | 每日活动摘要卡片 | history_list |
| `PerformanceScoreCard` | 表现评分卡（0–100） | weekly_review |
| `WorkoutVolumeCard` | 训练量统计卡 | active_workout |
| `TimerDisplay` | 训练计时器显示 | active_workout |
| `MultiWeekCompareChart` | 多周数据对比图（体重/热量/训练量） | weekly_comparison |
| `PRTimeline` | 个人记录时间线（各动作最佳成绩） | personal_records |
| `SummaryStatRow` | 训练总结数据行（时长/容量/PR数） | workout_summary |

### 3.3 录入组件

| 组件名 | 描述 | 使用页面 |
|--------|------|----------|
| `RulerSlider` | 横向刻度尺选择器 | record_body_stats |
| `SetRow` | 训练组数行（重量/次数/RPE 输入） | active_workout |
| `ExerciseCard` | 动作卡片（名称 + 图片 + 复选框） | exercise_picker |
| `FoodCard` | 食物卡片（名称 + 营养 + 图片 + 加号） | food_search_sheet |
| `FoodListItem` | 食物列表条目（名称 + 热量 + 删除） | meal_template_editor |
| `ExerciseListItem` | 动作列表条目（名称 + 组数配置 + 删除） | workout_template_editor |
| `AITextInput` | AI 食物描述文本输入框 | record_meal_sheet |
| `NutritionInputGrid` | 宏量手动输入网格 | record_meal_sheet |
| `OnboardingStep` | 引导步骤容器（进度指示 + 内容 + 下一步） | onboarding |
| `GoalFormGroup` | 目标配置表单组（目标体重/营养/训练频率） | goal_setup |
| `NotificationToggleRow` | 通知开关行（类型 + 时间 + Toggle） | notification_settings |
| `ImportFileDropzone` | 文件导入区域（支持拖拽/选择） | data_import |

### 3.4 卡片与容器

| 组件名 | 描述 | 使用页面 |
|--------|------|----------|
| `SummaryCard` | 今日摘要卡（阶段 + 进度） | today_dashboard |
| `BodyStatsCard` | 身体数据卡（体重/体脂/BMI） | today_dashboard |
| `NutritionSummaryCard` | 营养摘要卡（各餐热量） | today_dashboard |
| `WorkoutStatusCard` | 训练状态卡（名称 + 进度） | today_dashboard |
| `DailyNotesCard` | 每日笔记卡 | today_dashboard |
| `HydrationCard` | 水分摄入卡 | record_meal_sheet |
| `ActivityBurnCard` | 活动消耗卡 | record_meal_sheet |
| `WorkoutTemplateCard` | 训练模板卡（名称 + 动作数 + 使用次数） | template_gallery, start_workout_sheet |
| `MealTemplateCard` | 餐食模板卡（名称 + 热量 + 蛋白质） | template_gallery |
| `AIResultCanvas` | AI 识别结果视觉画布（图片 + 扫描框叠加） | ai_food_recognition |
| `InsightCard` | 洞察提示卡（营养/训练建议） | ai_food_recognition |
| `MissingLogCard` | 补记缺失项卡片 | backfill_journal |
| `ExportSummaryCard` | 导出摘要卡（数据量 + 文件大小） | export_data |
| `PrivacyCard` | 隐私/加密说明卡 | export_data |
| `ProfileStatsGrid` | 使用统计 Bento 网格（连续天/总天/训练次/训练量） | profile |
| `GoalProgressCard` | 目标进度卡（当前/目标体重 + 进度条） | profile |
| `WorkoutSummaryBento` | 训练完成数据 Bento（时长/容量/PR/疲劳度） | workout_summary |
| `ImportHistoryItem` | 导入历史记录条目 | data_import |

### 3.5 弹窗与选择器

| 组件名 | 描述 | 使用页面 |
|--------|------|----------|
| `BottomSheet` | 底部弹窗容器（含模糊背景） | quick_add_sheet, start_workout_sheet, exercise_picker, food_search_sheet |
| `QuickAddGrid` | 快速添加操作网格（2×2） | quick_add_sheet |
| `CategoryTabRow` | 分类标签横向滚动行 | exercise_picker, food_search_sheet |
| `SearchBar` | 搜索输入框 | exercise_picker, food_search_sheet |
| `TemplateDropdown` | 模板选择下拉 | record_meal_sheet |
| `TimeRangeSelector` | 时间范围按钮组（7天/30天/全部/自定义） | export_data |
| `FormatToggle` | 格式切换（CSV/JSON） | export_data |
| `ContentCheckboxGroup` | 导出内容复选框组 | export_data |
| `WeekSelectorRow` | 多周对比周次选择行 | weekly_comparison |

### 3.6 状态与反馈

| 组件名 | 描述 | 使用页面 |
|--------|------|----------|
| `CompletionBadge` | 完成状态徽章 | backfill_journal |
| `StreakBadge` | 连续记录天数徽章 | backfill_journal |
| `PRBadge` | 个人记录（PR）标记 | history_day_detail, workout_summary |
| `WeekProgressBar` | 周完成度进度条 | backfill_journal |
| `PastRecordBanner` | 「正在查看过去记录」警告横幅 | history_day_detail |
| `GradientButton` | 渐变主色调 CTA 按钮 | 所有关键操作页面 |
| `FatigueRatingRow` | 疲劳度评分行（RPE 1–10） | workout_summary |

---

## 四、设计系统规范摘要

| 要素 | 规范 |
|------|------|
| 设计语言 | Material Design 3 |
| 设计主题 | "The Kinetic Lab"（高精度运动实验室） |
| 主色调 | Action Green `#3fff8b → #13ea79`（135° 渐变） |
| 背景色 | Obsidian `#0e0e0e` |
| 卡片层级 | surface-container-low `#131313` → surface-container `#1a1a1a` → surface-container-high `#20201f` |
| 标题字体 | Manrope（展示/标题） |
| 正文字体 | Inter（数据标签/正文） |
| 圆角规范 | 按钮 `0.75rem`，卡片 `1rem` |
| 毛玻璃效果 | 70% 不透明度 + 20px backdrop-blur |
| 色彩模式 | 深色模式优先 |
