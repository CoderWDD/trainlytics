package com.csd.trainlytics.ui.onboarding

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.FitnessPhase
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh

@Composable
fun GoalSetupScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var ageText by remember { mutableStateOf(state.age?.toString() ?: "") }
    var heightText by remember { mutableStateOf(state.heightCm?.toString() ?: "") }
    var currentWeightText by remember { mutableStateOf(state.currentWeightKg?.toString() ?: "") }
    var targetWeightText by remember { mutableStateOf(state.targetWeightKg?.toString() ?: "") }
    var caloriesText by remember { mutableStateOf(state.targetCalories?.toString() ?: "") }
    var proteinText by remember { mutableStateOf(state.targetProteinG?.toString() ?: "") }
    var carbsText by remember { mutableStateOf(state.targetCarbsG?.toString() ?: "") }
    var fatText by remember { mutableStateOf(state.targetFatG?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = "编辑目标",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(24.dp))

        Text(text = "性别", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Gender.values().filter { it != Gender.NOT_SET }.forEach { g ->
                GoalSelectableChip(
                    label = g.displayName(),
                    selected = state.gender == g,
                    onClick = { viewModel.setGender(g) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        GoalTextField(label = "年龄", value = ageText, suffix = "岁") {
            ageText = it; viewModel.setAge(it.toIntOrNull())
        }
        Spacer(Modifier.height(12.dp))
        GoalTextField(label = "身高", value = heightText, suffix = "cm") {
            heightText = it; viewModel.setHeight(it.toFloatOrNull())
        }
        Spacer(Modifier.height(12.dp))
        GoalTextField(label = "当前体重", value = currentWeightText, suffix = "kg") {
            currentWeightText = it; viewModel.setCurrentWeight(it.toFloatOrNull())
        }
        Spacer(Modifier.height(12.dp))
        GoalTextField(label = "目标体重", value = targetWeightText, suffix = "kg") {
            targetWeightText = it; viewModel.setTargetWeight(it.toFloatOrNull())
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "健身阶段", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        FitnessPhase.values().forEach { phase ->
            GoalSelectableChip(
                label = phase.displayName(),
                selected = state.fitnessPhase == phase,
                onClick = { viewModel.setFitnessPhase(phase) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(12.dp))
        Text(text = "每周训练天数", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            (1..7).forEach { days ->
                GoalSelectableChip(
                    label = "$days",
                    selected = state.weeklyWorkoutDays == days,
                    onClick = { viewModel.setWeeklyWorkoutDays(days) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "目标周期", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(4, 8, 12).forEach { weeks ->
                GoalSelectableChip(
                    label = "$weeks 周",
                    selected = state.goalWeeks == weeks,
                    onClick = { viewModel.setGoalWeeks(weeks) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(text = "营养目标（可选）", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "留空将根据健身阶段自动计算",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(12.dp))
        GoalTextField(label = "每日热量目标", value = caloriesText, suffix = "kcal", keyboardType = KeyboardType.Decimal) {
            caloriesText = it; viewModel.setTargetCalories(it.toFloatOrNull())
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GoalTextField(
                label = "蛋白质",
                value = proteinText,
                suffix = "g",
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f)
            ) { proteinText = it; viewModel.setTargetProtein(it.toFloatOrNull()) }
            GoalTextField(
                label = "碳水",
                value = carbsText,
                suffix = "g",
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f)
            ) { carbsText = it; viewModel.setTargetCarbs(it.toFloatOrNull()) }
            GoalTextField(
                label = "脂肪",
                value = fatText,
                suffix = "g",
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f)
            ) { fatText = it; viewModel.setTargetFat(it.toFloatOrNull()) }
        }

        Spacer(Modifier.height(32.dp))
        if (state.isSaving) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GradientStart)
            }
        } else {
            GradientButton(text = "保存", onClick = { viewModel.completeOnboarding(onComplete) })
        }
        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun GoalSelectableChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected)
                    Brush.horizontalGradient(listOf(GradientStart.copy(alpha = 0.2f), GradientEnd.copy(alpha = 0.2f)))
                else
                    Brush.horizontalGradient(listOf(SurfaceContainerHigh, SurfaceContainerHigh))
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) GradientStart else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun GoalTextField(
    label: String,
    value: String,
    suffix: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        suffix = { Text(suffix) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GradientStart,
            focusedLabelColor = GradientStart,
            cursorColor = GradientStart
        )
    )
}
