package com.csd.trainlytics.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val totalSteps = 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(48.dp))

        // Step indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.currentStep > 0) {
                IconButton(onClick = { viewModel.prevStep() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = MaterialTheme.colorScheme.onSurface)
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(totalSteps) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == state.currentStep) 24.dp else 8.dp, 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (i <= state.currentStep) GradientStart
                                else SurfaceContainerHighest
                            )
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(32.dp))

        AnimatedContent(
            targetState = state.currentStep,
            transitionSpec = {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            },
            label = "onboarding_step"
        ) { step ->
            when (step) {
                0 -> StepPersonalInfo(
                    state = state,
                    onGender = viewModel::setGender,
                    onAge = viewModel::setAge,
                    onHeight = viewModel::setHeight,
                    onNext = { viewModel.nextStep() }
                )
                1 -> StepWeightGoal(
                    state = state,
                    onCurrentWeight = viewModel::setCurrentWeight,
                    onTargetWeight = viewModel::setTargetWeight,
                    onNext = { viewModel.nextStep() }
                )
                2 -> StepFitnessGoal(
                    state = state,
                    onPhase = viewModel::setFitnessPhase,
                    onWorkoutDays = viewModel::setWeeklyWorkoutDays,
                    onComplete = {
                        viewModel.completeOnboarding(onComplete)
                    },
                    isSaving = state.isSaving
                )
            }
        }
    }
}

@Composable
private fun StepPersonalInfo(
    state: OnboardingState,
    onGender: (Gender) -> Unit,
    onAge: (Int?) -> Unit,
    onHeight: (Float?) -> Unit,
    onNext: () -> Unit
) {
    var ageText by remember { mutableStateOf(state.age?.toString() ?: "") }
    var heightText by remember { mutableStateOf(state.heightCm?.toString() ?: "") }

    Column {
        Text(
            text = "关于你",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "帮助我们个性化你的健身计划",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        Text(text = "性别", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Gender.values().filter { it != Gender.NOT_SET }.forEach { g ->
                SelectableChip(
                    label = g.displayName(),
                    selected = state.gender == g,
                    onClick = { onGender(g) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        NumberTextField(label = "年龄", value = ageText, suffix = "岁",
            onValueChange = { ageText = it; onAge(it.toIntOrNull()) })

        Spacer(Modifier.height(12.dp))

        NumberTextField(label = "身高", value = heightText, suffix = "cm",
            onValueChange = { heightText = it; onHeight(it.toFloatOrNull()) })

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))
        GradientButton(text = "下一步", onClick = onNext)
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun StepWeightGoal(
    state: OnboardingState,
    onCurrentWeight: (Float?) -> Unit,
    onTargetWeight: (Float?) -> Unit,
    onNext: () -> Unit
) {
    var currentText by remember { mutableStateOf(state.currentWeightKg?.toString() ?: "") }
    var targetText by remember { mutableStateOf(state.targetWeightKg?.toString() ?: "") }

    Column {
        Text(
            text = "体重目标",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "设定你的起点和终点", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(32.dp))

        NumberTextField(label = "当前体重", value = currentText, suffix = "kg",
            onValueChange = { currentText = it; onCurrentWeight(it.toFloatOrNull()) })
        Spacer(Modifier.height(12.dp))
        NumberTextField(label = "目标体重", value = targetText, suffix = "kg",
            onValueChange = { targetText = it; onTargetWeight(it.toFloatOrNull()) })

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))
        GradientButton(text = "下一步", onClick = onNext)
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun StepFitnessGoal(
    state: OnboardingState,
    onPhase: (FitnessPhase) -> Unit,
    onWorkoutDays: (Int) -> Unit,
    onComplete: () -> Unit,
    isSaving: Boolean
) {
    Column {
        Text(
            text = "健身阶段",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "选择当前的训练目标", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))

        FitnessPhase.values().forEach { phase ->
            SelectableChip(
                label = phase.displayName(),
                selected = state.fitnessPhase == phase,
                onClick = { onPhase(phase) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(16.dp))
        Text(text = "每周训练天数", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..7).forEach { days ->
                SelectableChip(
                    label = "$days",
                    selected = state.weeklyWorkoutDays == days,
                    onClick = { onWorkoutDays(days) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))
        if (isSaving) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GradientStart)
            }
        } else {
            GradientButton(text = "开始使用", onClick = onComplete)
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SelectableChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
private fun NumberTextField(
    label: String,
    value: String,
    suffix: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        suffix = { Text(suffix) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GradientStart,
            focusedLabelColor = GradientStart,
            cursorColor = GradientStart
        )
    )
}
