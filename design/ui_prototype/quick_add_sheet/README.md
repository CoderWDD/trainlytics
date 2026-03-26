# 快速添加

所属模块: 今日 (Today)
页面类型: 底部弹窗 (Bottom Sheet)

## 页面说明

通过FAB按钮触发的快速添加底部弹窗，提供记录体重、添加餐食、开始训练的快速入口。

## 关键组件

- 模糊背景
- 底部弹窗: "快速添加"标题，关闭按钮
- 2x2操作网格: "记录体重"（秤图标），"添加餐食"（餐厅图标），"开始训练"（健身图标），"敬请期待"（锁定/禁用）

## 入口

- `today_dashboard` FAB "+"按钮

## 出口

- "记录体重" → `record_body_stats`
- "添加餐食" → `record_meal_sheet`
- "开始训练" → `start_workout_sheet`
- 关闭 → `today_dashboard`
