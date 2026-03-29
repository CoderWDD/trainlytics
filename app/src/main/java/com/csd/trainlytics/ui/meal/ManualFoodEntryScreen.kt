package com.csd.trainlytics.ui.meal

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun ManualFoodEntryScreen(
    onBack: () -> Unit,
    viewModel: ManualFoodEntryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var weightG by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = "添加食物",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "每 100g 的营养数据",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(20.dp))

        FoodField(label = "食物名称 *", value = name, keyboardType = KeyboardType.Text) { name = it }
        Spacer(Modifier.height(12.dp))
        FoodField(label = "重量 (g)", value = weightG, keyboardType = KeyboardType.Decimal) { weightG = it }
        Spacer(Modifier.height(20.dp))

        // Macro Bento grid with icons
        Text(
            text = "营养数据",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroInputCard(
                    icon = Icons.Filled.Whatshot,
                    label = "热量 (kcal) *",
                    value = calories,
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = { calories = it },
                    modifier = Modifier.weight(1f)
                )
                MacroInputCard(
                    icon = Icons.Filled.FitnessCenter,
                    label = "蛋白质 (g)",
                    value = protein,
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = { protein = it },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroInputCard(
                    icon = Icons.Filled.Scale,
                    label = "碳水 (g)",
                    value = carbs,
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = { carbs = it },
                    modifier = Modifier.weight(1f)
                )
                MacroInputCard(
                    icon = Icons.Filled.WaterDrop,
                    label = "脂肪 (g)",
                    value = fat,
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = { fat = it },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 精准校对 info box
        BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Filled.Info,
                    null,
                    tint = GradientStart,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "精准校对：输入食物重量后，系统将自动按比例计算实际摄入量",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        if (state.isSaving) {
            CircularProgressIndicator(color = GradientStart)
        } else {
            GradientButton(
                text = "保存食物",
                onClick = {
                    viewModel.save(
                        name = name,
                        calories100g = calories.toFloatOrNull() ?: 0f,
                        protein100g = protein.toFloatOrNull() ?: 0f,
                        carbs100g = carbs.toFloatOrNull() ?: 0f,
                        fat100g = fat.toFloatOrNull() ?: 0f,
                        onSaved = onBack
                    )
                }
            )
        }

        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun MacroInputCard(
    icon: ImageVector,
    label: String,
    value: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BentoCard(modifier = modifier, backgroundColor = SurfaceContainerLow) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GradientStart.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = GradientStart, modifier = Modifier.size(18.dp))
            }
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label, fontSize = 10.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientStart,
                    focusedLabelColor = GradientStart,
                    cursorColor = GradientStart
                )
            )
        }
    }
}

@Composable
private fun FoodField(label: String, value: String, keyboardType: KeyboardType, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GradientStart,
            focusedLabelColor = GradientStart,
            cursorColor = GradientStart
        )
    )
}
