package com.csd.trainlytics.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.MealTemplateItem
import kotlin.math.roundToInt

@Composable
fun MealTemplateEditorScreen(
    onBack: () -> Unit,
    viewModel: MealTemplateEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF3FFF8B),
        unfocusedBorderColor = Color(0xFF3A3A3A),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color(0xFF3FFF8B),
        focusedLabelColor = Color(0xFF3FFF8B),
        unfocusedLabelColor = Color(0xFF9B9B9B)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = Color.White)
            }
            Text(
                if (state.templateName.isBlank()) "新建饮食模板" else state.templateName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            item {
                OutlinedTextField(
                    value = state.templateName,
                    onValueChange = { viewModel.setName(it) },
                    label = { Text("模板名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors
                )
            }

            item {
                OutlinedTextField(
                    value = state.note,
                    onValueChange = { viewModel.setNote(it) },
                    label = { Text("备注（可选）") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = fieldColors
                )
            }

            // Totals summary
            if (state.items.isNotEmpty()) {
                item {
                    val totalCal = state.items.sumOf { it.calories.toDouble() }.toFloat()
                    val totalPro = state.items.sumOf { it.proteinG.toDouble() }.toFloat()
                    val totalCarb = state.items.sumOf { it.carbsG.toDouble() }.toFloat()
                    val totalFat = state.items.sumOf { it.fatG.toDouble() }.toFloat()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A1A1A))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "合计: ${totalCal.roundToInt()} kcal",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF3FFF8B)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("蛋白质 ${totalPro.roundToInt()}g", style = MaterialTheme.typography.bodySmall, color = Color(0xFF60A5FA))
                            Text("碳水 ${totalCarb.roundToInt()}g", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFACC15))
                            Text("脂肪 ${totalFat.roundToInt()}g", style = MaterialTheme.typography.bodySmall, color = Color(0xFFF97316))
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "食物列表",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White
                    )
                    IconButton(onClick = { viewModel.showAddItemForm() }) {
                        Icon(Icons.Filled.Add, contentDescription = "添加食物", tint = Color(0xFF3FFF8B))
                    }
                }
            }

            if (state.items.isEmpty() && !state.showAddItemForm) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A1A1A))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "点击 + 添加食物",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            itemsIndexed(state.items) { index, item ->
                MealTemplateItemRow(item = item, onRemove = { viewModel.removeItem(index) })
            }

            // Inline add item form
            if (state.showAddItemForm) {
                item {
                    AddMealItemForm(
                        foodName = state.newFoodName,
                        weightGrams = state.newWeightGrams,
                        calories = state.newCalories,
                        proteinG = state.newProteinG,
                        carbsG = state.newCarbsG,
                        fatG = state.newFatG,
                        onFoodNameChange = { viewModel.setNewFoodName(it) },
                        onWeightChange = { viewModel.setNewWeightGrams(it) },
                        onCaloriesChange = { viewModel.setNewCalories(it) },
                        onProteinChange = { viewModel.setNewProteinG(it) },
                        onCarbsChange = { viewModel.setNewCarbsG(it) },
                        onFatChange = { viewModel.setNewFatG(it) },
                        onAdd = { viewModel.addItem() },
                        onCancel = { viewModel.hideAddItemForm() },
                        fieldColors = fieldColors
                    )
                }
            }

            item {
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { viewModel.save() },
                    enabled = state.templateName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(
                            brush = if (state.templateName.isNotBlank())
                                Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79)))
                            else
                                Brush.linearGradient(listOf(Color(0xFF333333), Color(0xFF333333))),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "保存模板",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = if (state.templateName.isNotBlank()) Color(0xFF004820) else Color(0xFF666666)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MealTemplateItemRow(item: MealTemplateItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1A1A1A))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.foodName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )
            Text(
                "${item.weightGrams.roundToInt()}g · ${item.calories.roundToInt()} kcal",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9B9B9B)
            )
            if (item.proteinG > 0f || item.carbsG > 0f || item.fatG > 0f) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("P ${item.proteinG.roundToInt()}g", style = MaterialTheme.typography.labelSmall, color = Color(0xFF60A5FA))
                    Text("C ${item.carbsG.roundToInt()}g", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFACC15))
                    Text("F ${item.fatG.roundToInt()}g", style = MaterialTheme.typography.labelSmall, color = Color(0xFFF97316))
                }
            }
        }
        IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "移除",
                tint = Color(0xFF666666),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun AddMealItemForm(
    foodName: String,
    weightGrams: String,
    calories: String,
    proteinG: String,
    carbsG: String,
    fatG: String,
    onFoodNameChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onAdd: () -> Unit,
    onCancel: () -> Unit,
    fieldColors: androidx.compose.material3.TextFieldColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1A))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "添加食物",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        OutlinedTextField(
            value = foodName,
            onValueChange = onFoodNameChange,
            label = { Text("食物名称") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = fieldColors
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = weightGrams,
                onValueChange = onWeightChange,
                label = { Text("重量(g)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = fieldColors
            )
            OutlinedTextField(
                value = calories,
                onValueChange = onCaloriesChange,
                label = { Text("热量(kcal)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = fieldColors
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = proteinG,
                onValueChange = onProteinChange,
                label = { Text("蛋白质") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = fieldColors
            )
            OutlinedTextField(
                value = carbsG,
                onValueChange = onCarbsChange,
                label = { Text("碳水") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = fieldColors
            )
            OutlinedTextField(
                value = fatG,
                onValueChange = onFatChange,
                label = { Text("脂肪") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = fieldColors
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A))
            ) {
                Text("取消", color = Color(0xFF9B9B9B))
            }
            Button(
                onClick = onAdd,
                enabled = foodName.isNotBlank(),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3FFF8B),
                    disabledContainerColor = Color(0xFF333333)
                )
            ) {
                Text("添加", color = Color(0xFF004820))
            }
        }
    }
}
