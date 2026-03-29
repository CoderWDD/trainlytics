package com.csd.trainlytics.ui.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun RecordMealSheet(
    onDismiss: () -> Unit,
    onNavigateToAiRecognition: () -> Unit = {},
    viewModel: MealViewModel = hiltViewModel()
) {
    val state by viewModel.entryState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "记录饮食",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "手动输入食物信息",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(20.dp))

        // AI scan section
        Text(
            text = "AI 智能识别",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.aiTextInput,
            onValueChange = viewModel::updateAiText,
            placeholder = { Text("描述你吃了什么，例如：一碗米饭加两块鸡胸肉") },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            colors = outlinedFieldColors()
        )
        Spacer(Modifier.height(8.dp))
        GradientButton(
            text = "分析餐食",
            onClick = { onNavigateToAiRecognition() },
            enabled = state.aiTextInput.isNotBlank()
        )
        Spacer(Modifier.height(20.dp))

        // Meal type selector
        Text(
            text = "餐次",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MealType.entries.forEach { type ->
                MealTypeChip(
                    label = type.displayName(),
                    selected = state.mealType == type,
                    onClick = { viewModel.updateMealType(type) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        // Food name
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::updateName,
            label = { Text("食物名称 *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = outlinedFieldColors()
        )
        Spacer(Modifier.height(12.dp))

        // Calories
        OutlinedTextField(
            value = state.calories,
            onValueChange = viewModel::updateCalories,
            label = { Text("热量 (kcal) *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = outlinedFieldColors()
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "宏量营养素（可选）",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))

        // Macros row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.proteinG,
                onValueChange = viewModel::updateProtein,
                label = { Text("蛋白质 g") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedFieldColors()
            )
            OutlinedTextField(
                value = state.carbsG,
                onValueChange = viewModel::updateCarbs,
                label = { Text("碳水 g") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedFieldColors()
            )
            OutlinedTextField(
                value = state.fatG,
                onValueChange = viewModel::updateFat,
                label = { Text("脂肪 g") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedFieldColors()
            )
        }

        Spacer(Modifier.height(28.dp))

        GradientButton(
            text = if (state.isSaving) "保存中..." else "保存",
            onClick = {
                viewModel.saveRecord { onDismiss() }
            },
            enabled = !state.isSaving
        )
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun MealTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundBrush = if (selected) {
        Brush.linearGradient(listOf(GradientStart.copy(alpha = 0.25f), GradientEnd.copy(alpha = 0.25f)))
    } else null

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (backgroundBrush != null) Modifier.background(backgroundBrush)
                else Modifier.background(SurfaceContainerLow)
            )
            .then(
                if (selected) Modifier.border(1.dp, GradientStart, RoundedCornerShape(8.dp))
                else Modifier
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) GradientStart else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GradientStart,
    focusedLabelColor = GradientStart,
    cursorColor = GradientStart
)
