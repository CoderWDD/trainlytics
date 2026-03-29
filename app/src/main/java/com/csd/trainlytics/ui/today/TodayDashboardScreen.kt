package com.csd.trainlytics.ui.today

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.TodaySummary
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.CalorieRing
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.components.GradientProgressBar
import com.csd.trainlytics.ui.components.MacroRing
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.NeutralBlue
import com.csd.trainlytics.ui.theme.SurfaceContainer
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import com.csd.trainlytics.ui.theme.WarningAmber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TodayDashboardScreen(
    onNavigateToRecordBody: () -> Unit,
    onNavigateToRecordMeal: () -> Unit,
    onNavigateToStartWorkout: () -> Unit,
    onNavigateToActiveWorkout: (Long) -> Unit,
    onNavigateToQuickAdd: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    viewModel: TodayViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GradientStart)
        }
        return
    }

    val summary = state.summary
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("M月d日 EEEE")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToQuickAdd,
                containerColor = GradientStart,
                contentColor = Color(0xFF00210B),
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "快速记录", modifier = Modifier.size(28.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Header
            TodayHeader(date = today, formatter = dateFormatter, summary = summary, onNavigateToProfile = onNavigateToProfile)

            Spacer(Modifier.height(16.dp))

            if (summary != null) {
                // Phase badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PhaseBadge(phase = summary.fitnessPhase.displayName())
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${summary.completedItems}/${summary.totalItems} 完成",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Hero: Calorie + Macros card
                CalorieMacrosCard(summary = summary)

                Spacer(Modifier.height(12.dp))

                // 2-col row: Body Stats | Workout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BodyStatsCard(
                        summary = summary,
                        onNavigate = onNavigateToRecordBody,
                        modifier = Modifier.weight(1f)
                    )
                    WorkoutCard(
                        summary = summary,
                        onNavigateToStartWorkout = onNavigateToStartWorkout,
                        onNavigateToActiveWorkout = onNavigateToActiveWorkout,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Meals breakdown card
                MealsBreakdownCard(summary = summary, onNavigate = onNavigateToRecordMeal)

                Spacer(Modifier.height(12.dp))

                // Quick actions row
                QuickActionsRow(
                    onRecordBody = onNavigateToRecordBody,
                    onRecordMeal = onNavigateToRecordMeal,
                    onStartWorkout = onNavigateToStartWorkout
                )
            } else {
                // Empty / first launch state
                FirstLaunchPrompt(
                    onRecordBody = onNavigateToRecordBody,
                    onStartWorkout = onNavigateToStartWorkout
                )
            }

            Spacer(Modifier.height(100.dp)) // bottom nav clearance
        }
    }
}

@Composable
private fun TodayHeader(
    date: LocalDate,
    formatter: DateTimeFormatter,
    summary: TodaySummary?,
    onNavigateToProfile: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "今日",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = date.format(formatter),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .clickable { onNavigateToProfile() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "个人中心",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PhaseBadge(phase: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(listOf(GradientStart.copy(alpha = 0.2f), GradientEnd.copy(alpha = 0.2f)))
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = phase,
            color = GradientStart,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CalorieMacrosCard(summary: TodaySummary) {
    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Text(
                text = "今日营养",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalorieRing(
                    currentCal = summary.totalCalories,
                    targetCal = summary.targetCalories,
                    size = 110.dp
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        MacroRing(
                            progress = summary.totalCalories / summary.targetCalories.coerceAtLeast(1f),
                            label = "热量",
                            valueText = "${summary.totalCalories.toInt()}",
                            size = 64.dp,
                            strokeWidth = 6.dp,
                            progressColor = GradientStart
                        )
                    }
                    // Calorie goal label
                    Text(
                        text = "目标 ${summary.targetCalories.toInt()} kcal",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            GradientProgressBar(
                progress = if (summary.targetCalories > 0) summary.totalCalories / summary.targetCalories else 0f
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${summary.totalCalories.toInt()} kcal 已摄入",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
                Text(
                    text = "剩余 ${(summary.targetCalories - summary.totalCalories).toInt().coerceAtLeast(0)} kcal",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun BodyStatsCard(
    summary: TodaySummary,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    BentoCard(
        modifier = modifier
            .height(140.dp)
            .clickable { onNavigate() },
        backgroundColor = SurfaceContainerHigh,
        padding = 14.dp
    ) {
        Column {
            Icon(
                imageVector = Icons.Filled.Scale,
                contentDescription = null,
                tint = NeutralBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "体重",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = summary.latestWeight?.let { "${it} kg" } ?: "--",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            if (summary.bodyFatPercent != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${summary.bodyFatPercent}% 体脂",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "+ 记录体重",
                    style = MaterialTheme.typography.bodySmall,
                    color = GradientStart
                )
            }
        }
    }
}

@Composable
private fun WorkoutCard(
    summary: TodaySummary,
    onNavigateToStartWorkout: () -> Unit,
    onNavigateToActiveWorkout: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeSession = summary.activeWorkoutSession
    BentoCard(
        modifier = modifier
            .height(140.dp)
            .clickable {
                if (activeSession != null) onNavigateToActiveWorkout(activeSession.id)
                else onNavigateToStartWorkout()
            },
        backgroundColor = if (activeSession != null)
            GradientStart.copy(alpha = 0.12f) else SurfaceContainerHigh,
        padding = 14.dp
    ) {
        Column {
            Icon(
                imageVector = Icons.Filled.FitnessCenter,
                contentDescription = null,
                tint = if (activeSession != null) GradientStart else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (activeSession != null) "训练中" else "训练",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            if (activeSession != null) {
                Text(
                    text = activeSession.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = GradientStart,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "继续训练 →",
                    style = MaterialTheme.typography.bodySmall,
                    color = GradientStart
                )
            } else {
                Text(
                    text = summary.lastWorkoutSession?.let { "上次: ${it.session.name}" } ?: "开始训练",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "+ 开始训练",
                    style = MaterialTheme.typography.bodySmall,
                    color = GradientStart
                )
            }
        }
    }
}

@Composable
private fun MealsBreakdownCard(
    summary: TodaySummary,
    onNavigate: () -> Unit
) {
    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate() },
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "餐饮记录",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "+ 添加",
                    style = MaterialTheme.typography.labelMedium,
                    color = GradientStart
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MealBreakdownItem("早餐", summary.breakfastCalories)
                MealBreakdownItem("午餐", summary.lunchCalories)
                MealBreakdownItem("晚餐", summary.dinnerCalories)
                MealBreakdownItem("加餐", summary.snackCalories)
            }
        }
    }
}

@Composable
private fun MealBreakdownItem(name: String, calories: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (calories > 0) "${calories.toInt()}" else "--",
            style = MaterialTheme.typography.titleSmall,
            color = if (calories > 0) MaterialTheme.colorScheme.onSurface
                   else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "kcal",
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickActionsRow(
    onRecordBody: () -> Unit,
    onRecordMeal: () -> Unit,
    onStartWorkout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickActionChip(
            icon = Icons.Filled.Scale,
            label = "体重",
            onClick = onRecordBody,
            modifier = Modifier.weight(1f)
        )
        QuickActionChip(
            icon = Icons.Filled.Restaurant,
            label = "饮食",
            onClick = onRecordMeal,
            modifier = Modifier.weight(1f)
        )
        QuickActionChip(
            icon = Icons.Filled.DirectionsRun,
            label = "训练",
            onClick = onStartWorkout,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionChip(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerHighest)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GradientStart,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun FirstLaunchPrompt(
    onRecordBody: () -> Unit,
    onStartWorkout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "开始你的健身之旅",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "记录今日体重和训练，开启数据驱动的健身计划",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        GradientButton(text = "记录体重", onClick = onRecordBody)
        Spacer(Modifier.height(12.dp))
        GradientButton(text = "开始训练", onClick = onStartWorkout)
    }
}
