# Trainlytics 页面交互跳转流程

## 1. 应用整体架构

底部 **4 Tab** 导航 + 独立页面（设置、个人资料等通过入口跳转）。

首次启动走引导流程，完成后进入今日主页。

### 底部导航（4 个 Tab）

| Tab | 中文名 | 主页面 |
|-----|--------|--------|
| 1 | 今日 | `today_dashboard` |
| 2 | 历史 | `history_list` |
| 3 | 洞察 | `insights_dashboard` |
| 4 | 模板 | `template_gallery` |

---

## 2. 页面完整列表

共 **28 个页面**，按模块分类：

### 引导/启动（Launch）— 2 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 1 | `onboarding` | 引导欢迎页 | 全屏向导（首次启动） |
| 2 | `goal_setup` | 目标设置 | 全屏向导（首次 / 重新设置） |

### 今日模块（Today）— 6 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 3 | `today_dashboard` | 今日主页 | Tab 主页 |
| 4 | `quick_add_sheet` | 快速添加 | 底部弹窗 |
| 5 | `record_body_stats` | 身体数据记录 | 子页面 |
| 6 | `record_meal_sheet` | 餐食记录 | 子页面 |
| 7 | `start_workout_sheet` | 开始训练 | 底部弹窗 |
| 8 | `active_workout` | 训练进行中 | 全屏子页面 |

### 训练完成（Workout Completion）— 1 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 9 | `workout_summary` | 训练完成总结 | 全屏子页面 |

### 历史模块（History）— 3 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 10 | `history_list` | 历史记录 | Tab 主页 |
| 11 | `history_day_detail` | 单日详情 | 子页面 |
| 12 | `backfill_journal` | 补记日志 | 子页面 |

### 洞察模块（Insights）— 3 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 13 | `insights_dashboard` | 数据分析 | Tab 主页 |
| 14 | `weekly_review` | 每周回顾 | 子页面 |
| 15 | `weekly_comparison` | 多周对比 | 子页面 |

### 模板模块（Templates）— 3 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 16 | `template_gallery` | 模板管理 | Tab 主页 |
| 17 | `workout_template_editor` | 训练模板编辑 | 子页面 |
| 18 | `meal_template_editor` | 餐食模板编辑 | 子页面 |

### 通用组件（Shared）— 5 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 19 | `exercise_picker` | 选择动作 | 底部弹窗 |
| 20 | `food_search_sheet` | 食物搜索 | 底部弹窗 |
| 21 | `ai_food_recognition` | AI食物识别 | 子页面 |
| 22 | `manual_food_entry` | 手动食物录入 | 子页面 |
| 23 | `personal_records` | 个人记录（PR） | 子页面 |

### 设置/账户（Settings & Account）— 5 页

| # | 文件夹名 | 中文名 | 类型 |
|---|---------|--------|------|
| 24 | `settings` | 设置 | 独立页面 |
| 25 | `profile` | 个人资料 | 子页面 |
| 26 | `notification_settings` | 通知设置 | 子页面 |
| 27 | `export_data` | 导出数据 | 子页面 |
| 28 | `data_import` | 数据导入 | 子页面 |

---

## 3. 导航流程图

### 3.1 首次启动流程

```
首次启动
  └── onboarding（引导欢迎页，3步）
        └── goal_setup（目标设置）
              └── today_dashboard（进入主应用）
```

### 3.2 Tab 主导航

```
底部 Tab 导航
├── 今日 → today_dashboard
│     ├── FAB "+" → quick_add_sheet
│     │     ├── 记录体重 → record_body_stats
│     │     ├── 添加餐食 → record_meal_sheet
│     │     └── 开始训练 → start_workout_sheet
│     ├── 继续训练 → active_workout
│     └── 营养卡 → record_meal_sheet
│
├── 历史 → history_list
│     ├── 点击日期卡 → history_day_detail
│     └── 补记日志 → backfill_journal
│
├── 洞察 → insights_dashboard
│     ├── 每周回顾 → weekly_review
│     └── 多周对比 → weekly_comparison
│
└── 模板 → template_gallery
      ├── 训练模板 → workout_template_editor
      │     └── 添加动作 → exercise_picker
      └── 餐食模板 → meal_template_editor
            └── 添加食物 → food_search_sheet
                  ├── 手动输入 → manual_food_entry
                  └── AI识别 → ai_food_recognition
```

### 3.3 训练完整流程

```
start_workout_sheet（选择/空白/创建模板）
  └── active_workout（训练进行中）
        ├── 添加动作 → exercise_picker → 返回 active_workout
        └── 完成训练 → workout_summary（训练完成总结）
              └── 查看个人记录 → personal_records
                    返回 / → today_dashboard
```

### 3.4 设置/账户流程

```
settings（设置）入口：今日主页头像
├── 个人资料 → profile
│     └── 重新设置目标 → goal_setup
├── 通知设置 → notification_settings
├── 导出数据 → export_data
└── 数据导入 → data_import
```

### 3.5 餐食记录流程

```
record_meal_sheet
├── AI拍摄/描述 → ai_food_recognition → 确认返回
└── 手动录入 → manual_food_entry → 保存返回
```

### 3.6 补记日志流程

```
backfill_journal（补记日志）
├── 补记体重 → record_body_stats → 保存返回
├── 补记餐食 → record_meal_sheet → 保存返回
└── 补记训练 → start_workout_sheet → active_workout → workout_summary → 返回
```

---

## 4. 页面统计

| 模块 | Tab主页 | 全屏/子页面 | 底部弹窗 | 合计 |
|------|---------|-----------|---------|------|
| 引导/启动 | — | 2 | — | 2 |
| 今日 | 1 | 2 | 2 | 5 |
| 训练完成 | — | 1 | — | 1 |
| 历史 | 1 | 2 | — | 3 |
| 洞察 | 1 | 2 | — | 3 |
| 模板 | 1 | 2 | — | 3 |
| 通用组件 | — | 3 | 2 | 5 |
| 设置/账户 | — | 5 | — | 5 |
| 独立入口(active_workout) | — | 1 | — | 1 |
| **合计** | **4** | **20** | **4** | **28** |
