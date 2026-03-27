# Trainlytics 交互使用流程

> 使用 Mermaid 用例图描述整个项目的用户交互流程

---

## 一、系统参与者与用例总图

```mermaid
graph TB
    subgraph Actors["参与者"]
        User["👤 用户"]
        AI["🤖 AI识别引擎"]
        LocalDB["💾 本地数据库"]
    end

    subgraph App["Trainlytics 应用"]
        subgraph Launch["引导启动"]
            UC0a["首次引导"]
            UC0b["目标设置"]
        end

        subgraph Today["今日模块"]
            UC1["查看今日总览"]
            UC2["记录身体数据"]
            UC3["记录餐食"]
            UC4["开始训练"]
            UC5["进行训练中记录"]
            UC5b["查看训练完成总结"]
        end

        subgraph Nutrition["营养模块"]
            UC6["AI识别食物"]
            UC7["手动录入营养"]
            UC8["搜索食物数据库"]
        end

        subgraph History["历史模块"]
            UC9["浏览历史记录"]
            UC10["查看单日详情"]
            UC11["补记遗漏日志"]
        end

        subgraph Insights["洞察模块"]
            UC12["查看数据分析"]
            UC13["查看每周回顾"]
            UC13b["多周数据对比"]
        end

        subgraph Templates["模板模块"]
            UC14["管理训练模板"]
            UC15["管理餐食模板"]
            UC16["选择训练动作"]
        end

        subgraph Settings["设置模块"]
            UC17["配置应用设置"]
            UC17b["管理个人资料"]
            UC17c["配置通知提醒"]
            UC18["导出数据"]
            UC18b["导入数据"]
        end
    end

    User --> UC0a
    UC0a --> UC0b
    UC0b --> UC1

    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    UC5 --> UC5b
    UC5b --> UC19["查看个人记录PR"]

    User --> UC9
    User --> UC10
    User --> UC11
    User --> UC12
    User --> UC13
    User --> UC13b
    User --> UC14
    User --> UC15
    User --> UC17
    User --> UC17b
    User --> UC17c
    User --> UC18
    User --> UC18b

    UC3 --> UC6
    UC3 --> UC7
    UC6 --> AI
    UC8 --> LocalDB
    UC2 --> LocalDB
    UC3 --> LocalDB
    UC5 --> LocalDB
    UC18 --> LocalDB
    UC18b --> LocalDB
```

---

## 二、首次启动/引导流程

```mermaid
flowchart TD
    FirstLaunch(["首次启动"]) --> Onboarding["onboarding\n引导欢迎页（3步）"]

    Onboarding --> |"品牌介绍→功能亮点→权限申请"| GoalSetup["goal_setup\n目标设置"]

    GoalSetup --> |"填写身体数据\n设置目标体重/营养/训练频率"| Dashboard["today_dashboard\n今日总览（进入主应用）"]

    GoalSetup -. "展示内容" .-> SetupContent{{"• 当前体重/身高/性别/年龄\n• 目标体重 + 减重速度\n• 训练频率\n• 周期（周数）"}}
```

---

## 三、今日记录核心流程

```mermaid
flowchart TD
    Start(["🚀 打开应用"]) --> Dashboard["today_dashboard\n今日总览"]

    Dashboard --> |"FAB '+'"| QuickAdd["quick_add_sheet\n快速添加弹窗"]
    Dashboard --> |"继续训练"| ActiveWorkout["active_workout\n训练进行中"]
    Dashboard --> |"[记录]"| BodyStats["record_body_stats\n身体数据记录"]
    Dashboard --> |"营养卡"| MealSheet["record_meal_sheet\n餐食记录"]

    QuickAdd --> |"记录体重"| BodyStats
    QuickAdd --> |"添加餐食"| MealSheet
    QuickAdd --> |"开始训练"| StartWorkout["start_workout_sheet\n开始训练弹窗"]
    QuickAdd --> |"关闭"| Dashboard

    BodyStats --> |"保存记录"| Dashboard
    BodyStats --> |"X关闭"| Dashboard

    MealSheet --> |"分析餐食"| AIRecog["ai_food_recognition\nAI识别结果"]
    MealSheet --> |"手动录入"| ManualEntry["manual_food_entry\n手动食物录入"]
    MealSheet --> |"保存"| Dashboard

    AIRecog --> |"确认并添加"| MealSheet
    AIRecog --> |"微调"| AIRecog
    ManualEntry --> |"保存到餐食"| MealSheet

    StartWorkout --> |"选择模板"| ActiveWorkout
    StartWorkout --> |"空白训练"| ActiveWorkout
    StartWorkout --> |"创建新模板"| WorkoutEditor["workout_template_editor\n训练模板编辑"]

    ActiveWorkout --> |"添加动作"| ExercisePicker["exercise_picker\n选择动作"]
    ActiveWorkout --> |"完成训练"| WorkoutSummary["workout_summary\n训练完成总结"]

    ExercisePicker --> |"添加所选"| ActiveWorkout
    ExercisePicker --> |"关闭"| ActiveWorkout

    WorkoutSummary --> |"查看个人记录"| PersonalRecords["personal_records\n个人记录（PR）"]
    WorkoutSummary --> |"返回"| Dashboard

    PersonalRecords --> |"返回"| Dashboard

    WorkoutEditor --> |"保存模板"| StartWorkout
```

---

## 四、历史记录与补记流程

```mermaid
flowchart TD
    NavHistory["底部导航\n历史 Tab"] --> HistoryList["history_list\n历史记录主页"]

    HistoryList --> |"点击日期卡"| DayDetail["history_day_detail\n单日详情"]
    HistoryList --> |"补记日志"| BackfillJournal["backfill_journal\n补记日志"]

    DayDetail --> |"返回"| HistoryList

    BackfillJournal --> |"补记体重"| BodyStats["record_body_stats\n身体数据记录"]
    BackfillJournal --> |"补记餐食"| MealSheet["record_meal_sheet\n餐食记录"]
    BackfillJournal --> |"补记训练"| StartWorkout["start_workout_sheet\n开始训练弹窗"]
    BackfillJournal --> |"返回"| HistoryList

    BodyStats --> |"保存"| BackfillJournal
    MealSheet --> |"保存"| BackfillJournal
    StartWorkout --> |"开始训练"| ActiveWorkout["active_workout\n训练进行中"]
    ActiveWorkout --> |"完成训练"| BackfillJournal

    DayDetail -. "查看内容" .-> DetailContent{{"包含：\n体重/体脂数据\n训练动作详情（含PR）\n餐食明细\n宏量进度条"}}
    BackfillJournal -. "展示内容" .-> BackfillContent{{"包含：\n本周缺失日期列表\n缺失项类型标记\n周完成度进度条\n连续记录徽章"}}
```

---

## 五、数据洞察流程

```mermaid
flowchart TD
    NavInsights["底部导航\n洞察 Tab"] --> InsightsDash["insights_dashboard\n数据分析主页"]

    InsightsDash -. "展示内容" .-> DashContent{{"• 7/30日体重趋势折线图\n• 热量摄入柱状图\n• 蛋白质达成环形图\n• 关键动作1RM进展"}}

    InsightsDash --> |"每周回顾"| WeeklyReview["weekly_review\n每周回顾"]
    InsightsDash --> |"多周对比"| WeeklyComparison["weekly_comparison\n多周数据对比"]

    WeeklyReview -. "展示内容" .-> WeekContent{{"• 综合表现评分（0-100）\n• 本周体重变化柱状图\n• 营养均值与目标达成\n• 训练频率与总训练量\n• 主要动作1RM估算\n• 周笔记与下周目标"}}

    WeeklyComparison -. "展示内容" .-> CompContent{{"• 本周 vs 上周 vs 上周期\n• 体重/热量/蛋白质/训练量对比\n• 趋势方向指示"}}

    WeeklyReview --> |"返回"| InsightsDash
    WeeklyComparison --> |"返回"| InsightsDash
```

---

## 六、模板管理流程

```mermaid
flowchart TD
    NavTemplates["底部导航\n模板 Tab"] --> Gallery["template_gallery\n模板管理主页"]

    Gallery --> |"训练模板卡片"| WorkoutEditor["workout_template_editor\n训练模板编辑"]
    Gallery --> |"新建模板"| WorkoutEditor
    Gallery --> |"餐食模板卡片"| MealEditor["meal_template_editor\n餐食模板编辑"]
    Gallery --> |"FAB '+'"| QuickAdd["quick_add_sheet\n快速添加"]

    WorkoutEditor --> |"添加动作"| ExercisePicker["exercise_picker\n选择动作弹窗"]
    WorkoutEditor --> |"保存模板"| Gallery
    WorkoutEditor --> |"删除"| Gallery

    ExercisePicker --> |"添加所选(N)"| WorkoutEditor
    ExercisePicker --> |"关闭"| WorkoutEditor

    MealEditor --> |"添加食物"| FoodSearch["food_search_sheet\n食物搜索弹窗"]
    MealEditor --> |"保存模板"| Gallery
    MealEditor --> |"删除"| Gallery

    FoodSearch --> |"+ 直接添加"| MealEditor
    FoodSearch --> |"手动输入"| ManualEntry["manual_food_entry\n手动食物录入"]
    FoodSearch --> |"AI识别"| AIRecog["ai_food_recognition\nAI识别结果"]

    ManualEntry --> |"保存到餐食"| MealEditor
    AIRecog --> |"确认并添加"| MealEditor
```

---

## 七、设置与账户流程

```mermaid
flowchart TD
    AvatarMenu["今日主页\n头像入口"] --> Settings["settings\n设置页"]

    Settings --> |"个人资料"| Profile["profile\n个人资料"]
    Settings --> |"通知设置"| NotifSettings["notification_settings\n通知设置"]
    Settings --> |"导出数据"| ExportData["export_data\n导出数据"]
    Settings --> |"数据导入"| DataImport["data_import\n数据导入"]
    Settings --> |"返回"| PrevPage["上一页"]

    Profile --> |"重新设置目标"| GoalSetup["goal_setup\n目标设置"]
    GoalSetup --> |"保存"| Profile

    Profile -. "展示内容" .-> ProfileContent{{"• 头像/昵称\n• 基础信息（性别/年龄/身高/经验）\n• 目标进度（当前→目标体重）\n• 使用统计（连续/总天/训练次/训练量）"}}

    NotifSettings -. "配置项" .-> NotifContent{{"• 全局通知总开关\n• 餐食提醒（早/午/晚）\n• 训练提醒（训练日+时间）\n• 水分提醒（间隔+时段）\n• 每周回顾提醒（日期+时间）"}}

    ExportData --> |"选择格式"| FormatChoice{{"CSV / JSON"}}
    ExportData --> |"选择范围"| RangeChoice{{"7天 / 30天 / 全部 / 自定义"}}
    ExportData --> |"开始导出"| ExportAction["触发本地文件下载\n（AES-256加密）"]
    ExportData --> |"返回"| Settings

    DataImport -. "功能说明" .-> ImportContent{{"• 从JSON备份恢复\n• 从CSV迁移历史数据\n• 数据校验与冲突处理"}}

    Settings -. "配置项" .-> SettingsContent{{"• 体重/距离单位\n• 当前目标（减脂期）\n• 默认训练模板\n• 周起始日\n• 24小时制切换\n• 版本信息"}}
```

---

## 八、页面跳转关系图（全局视图）

```mermaid
graph TD
    subgraph Launch["首次启动"]
        onboarding --> goal_setup
        goal_setup -->|首次| today_dashboard
    end

    subgraph Nav["底部导航（持久）"]
        Tab1["今日"]
        Tab2["历史"]
        Tab3["洞察"]
        Tab4["模板"]
    end

    Tab1 --> today_dashboard
    Tab2 --> history_list
    Tab3 --> insights_dashboard
    Tab4 --> template_gallery

    today_dashboard -->|FAB| quick_add_sheet
    today_dashboard -->|继续训练| active_workout
    today_dashboard -->|记录| record_body_stats
    today_dashboard -->|营养卡| record_meal_sheet
    today_dashboard -->|头像| settings

    quick_add_sheet -->|记录体重| record_body_stats
    quick_add_sheet -->|添加餐食| record_meal_sheet
    quick_add_sheet -->|开始训练| start_workout_sheet

    record_meal_sheet -->|AI识别| ai_food_recognition
    record_meal_sheet -->|手动| manual_food_entry

    start_workout_sheet -->|选择/空白| active_workout
    start_workout_sheet -->|创建模板| workout_template_editor

    active_workout -->|添加动作| exercise_picker
    active_workout -->|完成训练| workout_summary

    workout_summary -->|查看PR| personal_records
    workout_summary -->|返回| today_dashboard

    history_list -->|点击日期| history_day_detail
    history_list -->|补记| backfill_journal

    backfill_journal -->|补记体重| record_body_stats
    backfill_journal -->|补记餐食| record_meal_sheet
    backfill_journal -->|补记训练| start_workout_sheet

    insights_dashboard -->|每周回顾| weekly_review
    insights_dashboard -->|多周对比| weekly_comparison

    template_gallery -->|训练模板| workout_template_editor
    template_gallery -->|餐食模板| meal_template_editor

    workout_template_editor -->|添加动作| exercise_picker
    meal_template_editor -->|添加食物| food_search_sheet

    food_search_sheet -->|手动输入| manual_food_entry
    food_search_sheet -->|AI识别| ai_food_recognition

    settings -->|个人资料| profile
    settings -->|通知设置| notification_settings
    settings -->|导出数据| export_data
    settings -->|数据导入| data_import

    profile -->|重设目标| goal_setup
```
