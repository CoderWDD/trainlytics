# 模板管理

所属模块: 模板 (Templates)
页面类型: 主页面 (Tab页)

## 页面说明
模板管理主页，展示所有训练和餐食模板，包含完成次数、宏量数据和快速导航到编辑器。

## 关键组件
- 头部"Trainlytics"
- "模板管理"标题
- 训练模板区(动力/TRAINING): 推日(6动作, 18组, 12次完成), 拉日(7动作, 21组, 8次), 腿日(5动作, 15组, 15次)——各带箭头导航和hover发光效果
- "新建模板"渐变按钮
- 餐食模板区(燃料/NUTRITION): 高蛋白早餐(520kcal, 35g蛋白质), 练后晚餐(680kcal, 48g蛋白质)
- "管理食谱"按钮
- FAB "+"快速添加

## 入口
- 底部导航"模板"Tab

## 出口
- 训练模板卡 → workout_template_editor
- 餐食模板卡 → meal_template_editor
- "新建模板" → workout_template_editor
- FAB → quick_add_sheet
- 底部导航切换
