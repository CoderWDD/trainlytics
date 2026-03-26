# Trainlytics Git 提交信息规范

基于 [Conventional Commits 1.0.0](https://www.conventionalcommits.org/zh-hans/) 规范。

## 格式

```
<type>(<scope>): <subject>

[body]

[footer]
```

## Type（必填）

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(ui): add today dashboard page` |
| `fix` | 修复缺陷 | `fix(data): correct weight trend calculation` |
| `docs` | 文档变更 | `docs(api): update README with setup guide` |
| `style` | 代码风格（不影响逻辑） | `style: format with ktlint` |
| `refactor` | 重构（非新功能、非修复） | `refactor(data): extract repository layer` |
| `perf` | 性能优化 | `perf(db): add index on workout table` |
| `test` | 测试相关 | `test(data): add BodyRecordDao unit tests` |
| `chore` | 构建/工具/依赖变更 | `chore: update Compose to 1.7.0` |
| `ci` | CI/CD 配置 | `ci: add GitHub Actions for lint` |
| `revert` | 回退提交 | `revert: revert feat(ui): add temp feature` |

## Scope（推荐）

按模块划分：

- `ui` — 界面/页面/组件
- `data` — 数据层（数据库、DAO、Repository）
- `domain` — 领域层（实体、用例）
- `design` — 设计稿/色彩系统/设计文档
- `ai` — AI 餐食识别
- `build` — 构建配置（Gradle、AGP）
- `docs` — 项目文档

## Subject（必填）

- 使用英文祈使句，动词原形开头，首字母小写
- 不超过 72 个字符
- 末尾不加句号
- 说明"做了什么"而非"怎么做的"

**Good:** `feat(ui): add weight recording page`
**Bad:** `feat(ui): Added the weight recording page.`
**Bad:** `update things`

## Body（可选）

- 与 subject 空一行
- 说明变更的动机、影响范围
- 可分多段

## Footer（可选）

- 与 body 空一行
- **Breaking Change:** `BREAKING CHANGE: description`
- **关联 Issue:** `Closes #123`

## 示例

### 简单提交

```
feat(ui): add meal recording page with AI recognition
```

### 带 body 的提交

```
feat(data): implement local database with Room

- Define BodyRecord, MealRecord, WorkoutSession entities
- Create DAOs with Flow-based reactive queries
- Add TypeConverters for date and enum types
```

### 带 Breaking Change 的提交

```
refactor(data)!: rename table columns to follow new naming convention

BREAKING CHANGE: All existing local data will be migrated automatically.
Migration path: version 1 -> 2 handled by Migration_1_2.
```

### 修复提交

```
fix(ui): resolve crash when navigating to history detail

The day detail page accessed body stats before lazy initialization.
Added null safety check and proper loading state.
```
