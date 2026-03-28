# ADR-001: Trainlytics 架构设计决策记录

## 项目概述
本地优先的Android减脂追踪应用，使用Jetpack Compose + Clean Architecture构建。

## 架构决策

### 1. 整体架构：Clean Architecture + MVI
- **Domain Layer**：纯Kotlin，实体、Repository接口、UseCases
- **Data Layer**：Room DB + DataStore + Repository实现
- **UI Layer**：Jetpack Compose + MVI ViewModel + StateFlow

### 2. 技术选型
| 技术栈 | 选型 | 理由 |
|--------|------|------|
| DI | Hilt | 官方推荐，编译期验证 |
| 数据库 | Room 2.7+ | 本地优先，支持Flow |
| 设置存储 | DataStore Preferences | 替代SharedPreferences，支持协程 |
| 导航 | Navigation-Compose | 与Compose深度集成 |
| 图表 | 自定义Canvas | 轻量，无第三方依赖 |
| 状态管理 | MVI + StateFlow | 单向数据流，可预测状态 |
| AI食物识别 | 预留接口 | 解耦，后续接入Claude/Gemini |

### 3. 包结构
```
com.csd.trainlytics/
├── core/
│   ├── designsystem/       # 主题、颜色、字体
│   └── navigation/         # 导航图、路由
├── domain/
│   ├── model/              # 领域实体
│   ├── repository/         # Repository接口
│   └── usecase/            # 用例
│       ├── body/
│       ├── meal/
│       ├── workout/
│       └── settings/
├── data/
│   ├── local/
│   │   ├── db/             # Room数据库、DAOs、实体
│   │   └── datastore/      # DataStore
│   ├── repository/         # Repository实现
│   └── di/                 # Hilt模块
└── feature/
    ├── today/              # 今日模块
    ├── history/            # 历史模块
    ├── insights/           # 洞察模块
    ├── templates/          # 模板模块
    ├── workout/            # 训练模块
    ├── nutrition/          # 营养模块
    ├── settings/           # 设置模块
    └── onboarding/         # 引导模块
```

### 4. 数据库设计（核心表）
- `body_records`：体重/体脂/腰围记录
- `meal_records`：餐食记录（含宏量）
- `food_items`：食物数据库
- `workout_sessions`：训练会话
- `workout_sets`：每组训练记录
- `exercises`：动作库
- `workout_templates`：训练模板
- `meal_templates`：餐食模板
- `user_goals`：用户目标

### 5. 设计系统（The Kinetic Lab）
- 深色优先：背景 #0e0e0e
- 主色：Action Green #3fff8b → #13ea79（135°渐变）
- 字体：Manrope（展示）+ Inter（数据）
- 毛玻璃导航栏：70%不透明度 + 20px blur
- 无分割线原则：靠色调层次区分
