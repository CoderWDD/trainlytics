package com.csd.trainlytics.ui.today

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.R
import com.csd.trainlytics.core.designsystem.MacroCalories
import com.csd.trainlytics.core.designsystem.MacroCarbs
import com.csd.trainlytics.core.designsystem.MacroFat
import com.csd.trainlytics.core.designsystem.MacroProtein
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
import com.csd.trainlytics.domain.model.MealType

private val PrimaryGradient = Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79)))
private val OnPrimaryColor = Color(0xFF004820)

@Composable
fun TodayScreen(
    onNavigateToRecordBodyStats: () -> Unit,
    onNavigateToRecordMeal: (MealType) -> Unit,
    onNavigateToActiveWorkout: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trainlytics",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                letterSpacing = (-0.5).sp
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighest),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.profile.name.firstOrNull()?.uppercase() ?: "U",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
        }

        // Page header
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "今日", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Primary, letterSpacing = (-0.5).sp)
            Text(text = state.todayLabel, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = OnSurfaceVariant, letterSpacing = 0.3.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Bento grid content
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Daily Summary + Weight row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Daily Summary card
                DailySummaryCard(
                    modifier = Modifier.weight(1.5f),
                    trainingPhase = state.trainingPhaseName,
                    mealsLogged = state.mealsLoggedCount,
                    totalMeals = state.totalMealSlots,
                    hasActiveSession = state.activeSession != null
                )
                // Weight card
                WeightCard(
                    modifier = Modifier.weight(1f),
                    weightKg = state.latestBodyRecord?.weightKg,
                    heightCm = state.profile.heightCm,
                    onRecordClick = onNavigateToRecordBodyStats
                )
            }

            // Nutrition card
            NutritionCard(
                meals = state.todayMeals,
                totalCalories = state.totalCalories,
                calorieTarget = state.calorieTarget,
                totalProtein = state.totalProtein,
                proteinTarget = state.proteinTarget,
                totalCarbs = state.totalCarbs,
                carbsTarget = state.carbsTarget,
                totalFat = state.totalFat,
                fatTarget = state.fatTarget,
                onAddMeal = onNavigateToRecordMeal
            )

            // Training card
            TrainingCard(
                sessions = state.todaySessions,
                activeSession = state.activeSession,
                onStartWorkout = onNavigateToActiveWorkout,
                onContinueWorkout = onNavigateToActiveWorkout
            )
        }

        Spacer(Modifier.height(100.dp)) // bottom nav + FAB clearance
    }
}

// ── Daily Summary Card ──────────────────────────────────────────────────────

@Composable
private fun DailySummaryCard(
    modifier: Modifier,
    trainingPhase: String,
    mealsLogged: Int,
    totalMeals: Int,
    hasActiveSession: Boolean
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "当前阶段", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant, letterSpacing = 1.sp)
                Text(text = trainingPhase, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = OnSurface, letterSpacing = (-0.3).sp)
            }
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceContainerHighest)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Primary)
                )
                Text(text = "已记录 $mealsLogged/$totalMeals", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Primary)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "今日方案", fontSize = 13.sp, color = OnSurfaceVariant)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    painter = painterResource(if (hasActiveSession) R.drawable.ic_today_filled else R.drawable.ic_history_outline),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (hasActiveSession) "训练进行中" else "训练日",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
            }
        }
    }
}

// ── Weight Card ─────────────────────────────────────────────────────────────

@Composable
private fun WeightCard(
    modifier: Modifier,
    weightKg: Float?,
    heightCm: Float?,
    onRecordClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "体重", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurface)
            Icon(
                painter = painterResource(R.drawable.ic_today_filled),
                contentDescription = "记录",
                tint = Primary,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onRecordClick() }
            )
        }
        Spacer(Modifier.height(8.dp))
        if (weightKg != null) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "%.1f".format(weightKg), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = OnSurface)
                Text(text = "kg", fontSize = 14.sp, color = OnSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
            }
        } else {
            Text(text = "—", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = OnSurfaceVariant)
            Text(text = "未记录", fontSize = 11.sp, color = OnSurfaceVariant)
        }
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(OutlineVariant.copy(alpha = 0.2f))
        ) {}
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "体脂率", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant, letterSpacing = 0.3.sp)
                Text(text = "—", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "BMI", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant, letterSpacing = 0.3.sp)
                val bmi = weightKg?.let { w ->
                    heightCm?.let { h ->
                        val hm = h / 100f
                        w / (hm * hm)
                    }
                }
                Text(text = bmi?.let { "%.1f".format(it) } ?: "—", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
            }
        }
    }
}

// ── Nutrition Card ──────────────────────────────────────────────────────────

@Composable
private fun NutritionCard(
    meals: List<com.csd.trainlytics.domain.model.MealRecord>,
    totalCalories: Float,
    calorieTarget: Int,
    totalProtein: Float,
    proteinTarget: Int,
    totalCarbs: Float,
    carbsTarget: Int,
    totalFat: Float,
    fatTarget: Int,
    onAddMeal: (MealType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "营养", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurface)
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "%.0f".format(totalCalories), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MacroCalories)
                Text(text = " / $calorieTarget kcal", fontSize = 13.sp, color = OnSurfaceVariant)
            }
        }

        // Macro progress bars
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MacroProgressRow(label = "蛋白质", value = totalProtein, target = proteinTarget.toFloat(), color = MacroProtein, unit = "g")
            MacroProgressRow(label = "碳水", value = totalCarbs, target = carbsTarget.toFloat(), color = MacroCarbs, unit = "g")
            MacroProgressRow(label = "脂肪", value = totalFat, target = fatTarget.toFloat(), color = MacroFat, unit = "g")
        }

        // Meal slots
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MealType.entries.forEach { mealType ->
                val recorded = meals.filter { it.mealType == mealType }
                if (recorded.isNotEmpty()) {
                    val totalMealCalories = recorded.sumOf { it.calories.toDouble() }.toFloat()
                    val totalMealProtein = recorded.sumOf { it.proteinG.toDouble() }.toFloat()
                    MealRow(
                        label = mealType.displayName,
                        timeAndCalories = "${recorded.first().let { r ->
                            val h = (r.recordedAt / 3600000 % 24).toInt()
                            val m = (r.recordedAt / 60000 % 60).toInt()
                            "%02d:%02d".format(h, m)
                        }} | %.0f 千卡".format(totalMealCalories),
                        proteinText = "蛋白质: %.0fg".format(totalMealProtein),
                        isRecorded = true,
                        onClick = { onAddMeal(mealType) }
                    )
                } else {
                    MealEmptyRow(
                        label = mealType.displayName,
                        onClick = { onAddMeal(mealType) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MacroProgressRow(label: String, value: Float, target: Float, color: Color, unit: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = OnSurfaceVariant)
            Text(text = "%.0f / %.0f$unit".format(value, target), fontSize = 12.sp, color = OnSurfaceVariant)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHighest)
        ) {
            val progress = if (target > 0) (value / target).coerceIn(0f, 1f) else 0f
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun MealRow(
    label: String,
    timeAndCalories: String,
    proteinText: String,
    isRecorded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainerHigh)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(painterResource(R.drawable.ic_today_filled), null, tint = Tertiary, modifier = Modifier.size(14.dp))
                Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
            }
            Text(text = timeAndCalories, fontSize = 11.sp, color = OnSurfaceVariant)
        }
        Text(text = proteinText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Tertiary)
    }
}

@Composable
private fun MealEmptyRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainer.copy(alpha = 0.5f))
            .border(1.dp, OutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = OnSurfaceVariant)
        Icon(painterResource(R.drawable.ic_today_filled), "添加", tint = OnSurfaceVariant, modifier = Modifier.size(18.dp))
    }
}

private val MealType.displayName: String
    get() = when (this) {
        MealType.BREAKFAST -> "早餐"
        MealType.LUNCH -> "午餐"
        MealType.DINNER -> "晚餐"
        MealType.SNACK -> "加餐"
    }

// ── Training Card ───────────────────────────────────────────────────────────

@Composable
private fun TrainingCard(
    sessions: List<com.csd.trainlytics.domain.model.WorkoutSession>,
    activeSession: com.csd.trainlytics.domain.model.WorkoutSession?,
    onStartWorkout: () -> Unit,
    onContinueWorkout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = activeSession?.templateName ?: if (sessions.isNotEmpty()) "今日训练" else "开始训练",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                if (activeSession != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PrimaryContainer))
                        val elapsed = System.currentTimeMillis() - activeSession.startedAt
                        val minutes = elapsed / 60000
                        Text(text = "进行中 | $minutes 分钟", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Primary)
                    }
                }
            }
            Icon(painterResource(R.drawable.ic_history_filled), null, tint = OnSurfaceVariant, modifier = Modifier.size(22.dp))
        }

        if (sessions.isNotEmpty() || activeSession != null) {
            val displaySession = activeSession ?: sessions.last()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = displaySession.sets.map { it.exerciseId }.distinct().size.toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = OnSurface
                    )
                    Text(text = "动作数量", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant, letterSpacing = 0.5.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(40.dp).background(OutlineVariant.copy(alpha = 0.2f)))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = displaySession.sets.size.toString(), fontSize = 30.sp, fontWeight = FontWeight.Black, color = OnSurface)
                    Text(text = "已完组数", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant, letterSpacing = 0.5.sp)
                }
                if (!displaySession.isCompleted) {
                    Spacer(Modifier.weight(1f))
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp), horizontalAlignment = Alignment.End) {
                        Text(text = "%.1f".format(displaySession.totalVolumeKg), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                        Text(text = "总重量 kg", fontSize = 9.sp, color = OnSurfaceVariant)
                    }
                }
            }
        }

        // CTA button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryGradient)
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    if (activeSession != null) onContinueWorkout() else onStartWorkout()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when {
                    activeSession != null -> "继续训练"
                    sessions.isNotEmpty() -> "再次训练"
                    else -> "开始训练"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OnPrimaryColor,
                letterSpacing = 0.3.sp
            )
        }
    }
}
