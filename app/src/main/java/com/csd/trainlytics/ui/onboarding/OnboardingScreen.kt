package com.csd.trainlytics.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.R
import com.csd.trainlytics.core.designsystem.OnSurface
import com.csd.trainlytics.core.designsystem.OnSurfaceVariant
import com.csd.trainlytics.core.designsystem.OutlineVariant
import com.csd.trainlytics.core.designsystem.Primary
import com.csd.trainlytics.core.designsystem.PrimaryContainer
import com.csd.trainlytics.core.designsystem.Surface
import com.csd.trainlytics.core.designsystem.SurfaceContainer
import com.csd.trainlytics.core.designsystem.SurfaceContainerHigh
import com.csd.trainlytics.core.designsystem.SurfaceContainerHighest
import com.csd.trainlytics.core.designsystem.SurfaceContainerLow
import com.csd.trainlytics.core.designsystem.Tertiary
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.domain.model.TrainingPhase

private val PrimaryGradient = Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79)))
private val OnPrimaryColor = Color(0xFF004820)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trainlytics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "跳过",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = OnSurfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSkip() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Step indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(state.totalSteps) { index ->
                val isActive = index + 1 == state.step
                val isCompleted = index + 1 < state.step
                val dotWidth by animateDpAsState(
                    targetValue = if (isActive) 40.dp else 8.dp,
                    animationSpec = tween(300),
                    label = "dot_width_$index"
                )
                val dotColor by animateColorAsState(
                    targetValue = when {
                        isActive -> Primary
                        isCompleted -> Primary.copy(alpha = 0.5f)
                        else -> SurfaceContainerHigh
                    },
                    animationSpec = tween(300),
                    label = "dot_color_$index"
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(dotWidth)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${state.step} / ${state.totalSteps}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = OnSurfaceVariant
            )
        }

        // Step content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (state.step) {
                1 -> StepWelcome(onNext = { viewModel.nextStep() })
                2 -> StepBodyData(
                    state = state,
                    onNameChange = viewModel::updateName,
                    onGenderChange = viewModel::updateGender,
                    onAgeChange = viewModel::updateAge,
                    onHeightChange = viewModel::updateHeight,
                    onWeightChange = viewModel::updateWeight,
                    onBack = { viewModel.prevStep() },
                    onNext = { viewModel.nextStep() }
                )
                3 -> StepTrainingPhase(
                    selected = state.trainingPhase,
                    onPhaseChange = viewModel::updatePhase,
                    onBack = { viewModel.prevStep() },
                    onNext = { viewModel.nextStep() }
                )
                4 -> StepGoals(
                    state = state,
                    onTargetWeightChange = viewModel::updateTargetWeight,
                    onFrequencyChange = viewModel::updateWorkoutFrequency,
                    onDurationChange = viewModel::updateDuration,
                    onBack = { viewModel.prevStep() },
                    onDone = { viewModel.completeOnboarding(onComplete) }
                )
            }
        }
    }
}

// ── Step 1: Welcome ─────────────────────────────────────────────────────────

@Composable
private fun StepWelcome(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            // Hero
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(PrimaryGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_today_filled),
                        contentDescription = null,
                        tint = Color(0xFF005d2c),
                        modifier = Modifier.size(48.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "欢迎使用",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Trainlytics",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        style = androidx.compose.ui.text.TextStyle(
                            brush = PrimaryGradient,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "本地优先的减脂期健身追踪\n科学记录，专注进步",
                        fontSize = 15.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 22.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // Feature highlights
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureCard(
                    icon = R.drawable.ic_today_filled,
                    iconTint = Primary,
                    iconBg = Primary.copy(alpha = 0.1f),
                    title = "AI 智能饮食识别",
                    subtitle = "拍照或描述即可记录宏量"
                )
                FeatureCard(
                    icon = R.drawable.ic_insights_filled,
                    iconTint = Tertiary,
                    iconBg = Tertiary.copy(alpha = 0.1f),
                    title = "力量进展追踪",
                    subtitle = "记录每组数据，估算1RM"
                )
                FeatureCard(
                    icon = R.drawable.ic_templates_filled,
                    iconTint = OnSurfaceVariant,
                    iconBg = SurfaceContainerHighest,
                    title = "数据完全本地",
                    subtitle = "AES-256 加密，数据属于你"
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        GradientButton(text = "开始设置", onClick = onNext)
    }
}

@Composable
private fun FeatureCard(
    icon: Int,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainer)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
            Text(text = subtitle, fontSize = 12.sp, color = OnSurfaceVariant)
        }
    }
}

// ── Step 2: Body Data ───────────────────────────────────────────────────────

@Composable
private fun StepBodyData(
    state: OnboardingState,
    onNameChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onAgeChange: (Int) -> Unit,
    onHeightChange: (Float) -> Unit,
    onWeightChange: (Float) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "基础身体数据",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                Text(text = "用于计算 BMR 和个性化建议", fontSize = 14.sp, color = OnSurfaceVariant)
            }

            // Gender selection
            SectionLabel("性别")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GenderCard(
                    label = "男性",
                    isSelected = state.gender == Gender.MALE,
                    modifier = Modifier.weight(1f),
                    onClick = { onGenderChange(Gender.MALE) }
                )
                GenderCard(
                    label = "女性",
                    isSelected = state.gender == Gender.FEMALE,
                    modifier = Modifier.weight(1f),
                    onClick = { onGenderChange(Gender.FEMALE) }
                )
            }

            // Age & Height
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SliderCard(
                    modifier = Modifier.weight(1f),
                    label = "年龄",
                    unit = "岁",
                    value = state.ageYears.toFloat(),
                    valueText = state.ageYears.toString(),
                    range = 15f..80f,
                    onValueChange = { onAgeChange(it.toInt()) }
                )
                SliderCard(
                    modifier = Modifier.weight(1f),
                    label = "身高",
                    unit = "cm",
                    value = state.heightCm,
                    valueText = state.heightCm.toInt().toString(),
                    range = 140f..220f,
                    onValueChange = { onHeightChange(it) }
                )
            }

            // Weight
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel("当前体重")
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "%.1f".format(state.currentWeightKg),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Text(text = "kg", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = OnSurfaceVariant)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Slider(
                        value = state.currentWeightKg,
                        onValueChange = onWeightChange,
                        valueRange = 30f..200f,
                        colors = SliderDefaults.colors(
                            thumbColor = Primary,
                            activeTrackColor = Primary,
                            inactiveTrackColor = SurfaceContainerHighest
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "30 kg", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text(text = "200 kg", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        TwoButtonRow(
            backText = "返回",
            nextText = "继续",
            onBack = onBack,
            onNext = onNext
        )
    }
}

@Composable
private fun GenderCard(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Primary else Color.Transparent,
        label = "gender_border"
    )
    val textColor = if (isSelected) OnSurface else OnSurfaceVariant
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainer)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}

@Composable
private fun SliderCard(
    modifier: Modifier,
    label: String,
    unit: String,
    value: Float,
    valueText: String,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant, letterSpacing = 0.8.sp)
        Text(text = valueText, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Primary)
        Text(text = unit, fontSize = 11.sp, color = OnSurfaceVariant)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Primary,
                inactiveTrackColor = SurfaceContainerHighest
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ── Step 3: Training Phase ──────────────────────────────────────────────────

@Composable
private fun StepTrainingPhase(
    selected: TrainingPhase,
    onPhaseChange: (TrainingPhase) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "你的目标是什么？", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(text = "这将影响营养目标和训练建议", fontSize = 14.sp, color = OnSurfaceVariant)
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PhaseCard(
                    phase = TrainingPhase.FAT_LOSS,
                    label = "减脂期",
                    description = "热量缺口 + 高蛋白，保留肌肉同时减脂",
                    badge = "推荐",
                    iconTint = Primary,
                    iconBg = Primary.copy(alpha = 0.1f),
                    selected = selected == TrainingPhase.FAT_LOSS,
                    onClick = { onPhaseChange(TrainingPhase.FAT_LOSS) }
                )
                PhaseCard(
                    phase = TrainingPhase.MAINTENANCE,
                    label = "维持期",
                    description = "热量平衡，维持当前体重和体型",
                    badge = null,
                    iconTint = OnSurfaceVariant,
                    iconBg = SurfaceContainerHighest,
                    selected = selected == TrainingPhase.MAINTENANCE,
                    onClick = { onPhaseChange(TrainingPhase.MAINTENANCE) }
                )
                PhaseCard(
                    phase = TrainingPhase.MUSCLE_GAIN,
                    label = "增肌期",
                    description = "热量盈余 + 渐进超负荷，增肌增力",
                    badge = null,
                    iconTint = OnSurfaceVariant,
                    iconBg = SurfaceContainerHighest,
                    selected = selected == TrainingPhase.MUSCLE_GAIN,
                    onClick = { onPhaseChange(TrainingPhase.MUSCLE_GAIN) }
                )
                PhaseCard(
                    phase = TrainingPhase.RECOMPOSITION,
                    label = "重组期",
                    description = "同步减脂和增肌，适合有一定训练基础者",
                    badge = null,
                    iconTint = Tertiary,
                    iconBg = Tertiary.copy(alpha = 0.1f),
                    selected = selected == TrainingPhase.RECOMPOSITION,
                    onClick = { onPhaseChange(TrainingPhase.RECOMPOSITION) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        TwoButtonRow(backText = "返回", nextText = "继续", onBack = onBack, onNext = onNext)
    }
}

@Composable
private fun PhaseCard(
    phase: TrainingPhase,
    label: String,
    description: String,
    badge: String?,
    iconTint: Color,
    iconBg: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) Primary else Color.Transparent,
        label = "phase_border_${phase.name}"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    when (phase) {
                        TrainingPhase.FAT_LOSS -> R.drawable.ic_today_filled
                        TrainingPhase.MUSCLE_GAIN -> R.drawable.ic_history_filled
                        TrainingPhase.MAINTENANCE -> R.drawable.ic_insights_filled
                        TrainingPhase.RECOMPOSITION -> R.drawable.ic_templates_filled
                    }
                ),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                if (badge != null) {
                    Text(
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Text(text = description, fontSize = 12.sp, color = OnSurfaceVariant, lineHeight = 17.sp)
        }
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (selected) Primary else Color.Transparent)
                .border(2.dp, if (selected) Primary else OnSurfaceVariant, CircleShape)
        )
    }
}

// ── Step 4: Goals ───────────────────────────────────────────────────────────

@Composable
private fun StepGoals(
    state: OnboardingState,
    onTargetWeightChange: (Float) -> Unit,
    onFrequencyChange: (Int) -> Unit,
    onDurationChange: (Int) -> Unit,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "设置你的目标", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(text = "根据目标计算个性化训练计划", fontSize = 14.sp, color = OnSurfaceVariant)
            }

            // Target weight
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel("目标体重")
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "%.1f".format(state.targetWeightKg), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Text(text = "kg", fontSize = 14.sp, color = OnSurfaceVariant)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Slider(
                        value = state.targetWeightKg,
                        onValueChange = onTargetWeightChange,
                        valueRange = 30f..200f,
                        colors = SliderDefaults.colors(
                            thumbColor = Primary,
                            activeTrackColor = Primary,
                            inactiveTrackColor = SurfaceContainerHighest
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "30 kg", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text(text = "200 kg", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }
            }

            // Workout frequency and duration
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SliderCard(
                    modifier = Modifier.weight(1f),
                    label = "每周训练",
                    unit = "次/周",
                    value = state.weeklyWorkoutFrequency.toFloat(),
                    valueText = state.weeklyWorkoutFrequency.toString(),
                    range = 1f..7f,
                    onValueChange = { onFrequencyChange(it.toInt()) }
                )
                SliderCard(
                    modifier = Modifier.weight(1f),
                    label = "计划周期",
                    unit = "周",
                    value = state.durationWeeks.toFloat(),
                    valueText = state.durationWeeks.toString(),
                    range = 4f..52f,
                    onValueChange = { onDurationChange(it.toInt()) }
                )
            }

            // Calorie preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_insights_filled),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                    Text(text = "预计每日热量目标", fontSize = 12.sp, color = OnSurfaceVariant)
                    val estimatedCalories = (state.currentWeightKg * 22 * 0.85).toInt()
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "%,d".format(estimatedCalories), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Text(text = "kcal", fontSize = 13.sp, color = OnSurfaceVariant)
                    }
                }
                Text(text = "可后续调整", fontSize = 11.sp, color = OnSurfaceVariant)
            }
        }

        Spacer(Modifier.height(24.dp))

        TwoButtonRow(backText = "返回", nextText = "开始使用", onBack = onBack, onNext = onDone)
    }
}

// ── Shared components ───────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = OnSurfaceVariant,
        letterSpacing = 0.8.sp
    )
}

@Composable
fun GradientButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(PrimaryGradient)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = OnPrimaryColor,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun TwoButtonRow(backText: String, nextText: String, onBack: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, OutlineVariant, RoundedCornerShape(12.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = backText, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant)
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryGradient)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onNext() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = nextText, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OnPrimaryColor, letterSpacing = 0.3.sp)
        }
    }
}
