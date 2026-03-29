package com.csd.trainlytics.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun MealTemplateEditorScreen(
    templateId: Long,
    onBack: () -> Unit,
    viewModel: MealTemplateEditorViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val template = state.templates.find { it.template.id == templateId }

    var nameText by remember { mutableStateOf("") }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(template) {
        if (template != null && nameText.isEmpty()) {
            nameText = template.template.name
        }
    }

    var newItemName by remember { mutableStateOf("") }
    var newItemAmount by remember { mutableStateOf("") }
    var newItemCalories by remember { mutableStateOf("") }
    var newItemProtein by remember { mutableStateOf("") }
    var newItemCarbs by remember { mutableStateOf("") }
    var newItemFat by remember { mutableStateOf("") }

    if (showAddItemDialog) {
        AlertDialog(
            onDismissRequest = { showAddItemDialog = false },
            title = { Text("添加食物") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("食物名称") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GradientStart,
                            focusedLabelColor = GradientStart,
                            cursorColor = GradientStart
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newItemAmount,
                            onValueChange = { newItemAmount = it },
                            label = { Text("克重 g") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GradientStart,
                                focusedLabelColor = GradientStart,
                                cursorColor = GradientStart
                            )
                        )
                        OutlinedTextField(
                            value = newItemCalories,
                            onValueChange = { newItemCalories = it },
                            label = { Text("热量 kcal") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GradientStart,
                                focusedLabelColor = GradientStart,
                                cursorColor = GradientStart
                            )
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = newItemProtein,
                            onValueChange = { newItemProtein = it },
                            label = { Text("蛋白质") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GradientStart,
                                focusedLabelColor = GradientStart,
                                cursorColor = GradientStart
                            )
                        )
                        OutlinedTextField(
                            value = newItemCarbs,
                            onValueChange = { newItemCarbs = it },
                            label = { Text("碳水") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GradientStart,
                                focusedLabelColor = GradientStart,
                                cursorColor = GradientStart
                            )
                        )
                        OutlinedTextField(
                            value = newItemFat,
                            onValueChange = { newItemFat = it },
                            label = { Text("脂肪") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GradientStart,
                                focusedLabelColor = GradientStart,
                                cursorColor = GradientStart
                            )
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newItemName.isNotBlank() && newItemCalories.toFloatOrNull() != null) {
                            viewModel.addItemWithMacros(
                                templateId = templateId,
                                name = newItemName,
                                amountG = newItemAmount.toFloatOrNull() ?: 0f,
                                calories = newItemCalories.toFloat(),
                                proteinG = newItemProtein.toFloatOrNull() ?: 0f,
                                carbsG = newItemCarbs.toFloatOrNull() ?: 0f,
                                fatG = newItemFat.toFloatOrNull() ?: 0f,
                                order = template?.items?.size ?: 0
                            )
                            showAddItemDialog = false
                            newItemName = ""; newItemAmount = ""; newItemCalories = ""
                            newItemProtein = ""; newItemCarbs = ""; newItemFat = ""
                        }
                    }
                ) { Text("添加", color = GradientStart) }
            },
            dismissButton = {
                TextButton(onClick = { showAddItemDialog = false }) { Text("取消") }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("删除模板") },
            text = { Text("确定删除「${template?.template?.name}」？此操作无法撤销。") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTemplate(templateId)
                    showDeleteDialog = false
                    onBack()
                }) { Text("删除", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("取消") }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = "编辑饮食模板",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    Icons.Filled.Delete,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("模板名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        cursorColor = GradientStart
                    )
                )
            }

            // Macro summary Bento card
            if (template != null && (template.totalCalories > 0f || template.items.isNotEmpty())) {
                item {
                    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "营养汇总",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                MacroCell(label = "热量", value = "${template.totalCalories.toInt()} kcal", isHighlighted = true)
                                MacroCell(label = "蛋白质", value = "${template.totalProteinG.toInt()} g")
                                MacroCell(label = "碳水", value = "${template.totalCarbsG.toInt()} g")
                                MacroCell(label = "脂肪", value = "${template.totalFatG.toInt()} g")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "食物列表 (${template?.items?.size ?: 0} 项)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            items(template?.items ?: emptyList(), key = { it.id }) { item ->
                MealTemplateItemRow(
                    item = item,
                    onDelete = { viewModel.deleteItem(item.id) }
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(GradientStart.copy(alpha = 0.1f))
                        .clickable { showAddItemDialog = true }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, null, tint = GradientStart)
                        Spacer(Modifier.width(8.dp))
                        Text("添加食物", color = GradientStart, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            item {
                GradientButton(
                    text = "保存模板",
                    onClick = {
                        if (nameText.isNotBlank()) {
                            viewModel.updateTemplateName(templateId, nameText)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun MacroCell(label: String, value: String, isHighlighted: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = if (isHighlighted) GradientStart else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun MealTemplateItemRow(item: MealTemplateItem, onDelete: () -> Unit) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.foodName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (item.amountG > 0f) {
                        Text(
                            text = "${item.amountG.toInt()}g",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "${item.calories.toInt()} kcal",
                        color = GradientStart,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (item.proteinG > 0f || item.carbsG > 0f || item.fatG > 0f) {
                    Text(
                        text = "P ${item.proteinG.toInt()}g · C ${item.carbsG.toInt()}g · F ${item.fatG.toInt()}g",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
