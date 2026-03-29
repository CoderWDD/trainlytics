package com.csd.trainlytics.ui.history

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HistoryDayDetailScreen(
    date: String,
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.dayDetailState.collectAsState()
    val parsedDate = runCatching { LocalDate.parse(date) }.getOrElse { LocalDate.now() }
    val isToday = parsedDate == LocalDate.now()

    LaunchedEffect(parsedDate) {
        viewModel.selectDate(parsedDate)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = parsedDate.format(DateTimeFormatter.ofPattern("M月d日 EEEE")),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Warning banner for past records
        if (!isToday) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "您正在查看过去的记录",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Body composition card
        state.bodyRecord?.let { record ->
            item {
                BodyCompositionCard(record = record)
            }
        }

        // Workout cards
        if (state.sessionsWithSets.isNotEmpty()) {
            items(state.sessionsWithSets) { sessionWithSets ->
                WorkoutDetailCard(sessionWithSets = sessionWithSets)
            }
        } else if (state.bodyRecord == null && !state.isLoading) {
            item {
                BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                    Text(
                        text = "当日无训练记录",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Nutrition section
        if (state.meals.isNotEmpty()) {
            item {
                NutritionSection(
                    meals = state.meals,
                    totalProtein = state.nutritionSummary?.totalProteinG ?: 0f,
                    totalCarbs = state.nutritionSummary?.totalCarbsG ?: 0f,
                    totalFat = state.nutritionSummary?.totalFatG ?: 0f,
                    targetProtein = state.userGoal?.targetProteinG ?: 150f,
                    targetCarbs = state.userGoal?.targetCarbsG ?: 200f,
                    targetFat = state.userGoal?.targetFatG ?: 60f
                )
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun BodyCompositionCard(record: BodyRecord) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Text(
                text = "身体成分",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                record.weightKg?.let { weight ->
                    BodyStatItem(
                        label = "体重",
                        value = "%.1f kg".format(weight),
                        modifier = Modifier.weight(1f)
                    )
                }
                record.bodyFatPercent?.let { bf ->
                    BodyStatItem(
                        label = "体脂率",
                        value = "%.1f%%".format(bf),
                        modifier = Modifier.weight(1f)
                    )
                }
                record.waistCm?.let { waist ->
                    BodyStatItem(
                        label = "腰围",
                        value = "%.0f cm".format(waist),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            record.notes?.let { notes ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BodyStatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = GradientStart,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WorkoutDetailCard(sessionWithSets: WorkoutSessionWithSets) {
    val session = sessionWithSets.session
    val setsByExercise = sessionWithSets.sets.groupBy { it.exerciseId }

    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GradientStart.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = GradientStart,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = session.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    session.durationMinutes?.let { mins ->
                        val h = mins / 60
                        val m = mins % 60
                        val durationText = if (h > 0) "${h}h ${m}m" else "${m}分钟"
                        Text(
                            text = durationText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (setsByExercise.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                setsByExercise.forEach { (_, sets) ->
                    val exerciseName = sets.firstOrNull()?.exerciseName ?: "未知动作"
                    val completedSets = sets.filter { it.isCompleted }
                    val topSet = completedSets.maxByOrNull { it.weightKg ?: 0f }
                    val hasPr = completedSets.any { it.estimatedOneRepMax != null }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHighest)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exerciseName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                            topSet?.let { set ->
                                val detail = buildString {
                                    append("${completedSets.size}组")
                                    set.weightKg?.let { append(" · ${it.toInt()}kg") }
                                }
                                Text(
                                    text = detail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (hasPr) {
                            Spacer(Modifier.width(8.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFFD700).copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.EmojiEvents,
                                    contentDescription = "PR",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "PR",
                                    color = Color(0xFFFFD700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun NutritionSection(
    meals: List<MealRecord>,
    totalProtein: Float,
    totalCarbs: Float,
    totalFat: Float,
    targetProtein: Float,
    targetCarbs: Float,
    targetFat: Float
) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Restaurant,
                    contentDescription = null,
                    tint = GradientStart,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "饮食记录",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(12.dp))

            // Meal list
            meals.forEach { meal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = meal.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = meal.mealType.displayName(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${meal.calories.toInt()} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GradientStart,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Macro progress bars
            MacroProgressBar(
                label = "蛋白质",
                current = totalProtein,
                target = targetProtein,
                unit = "g",
                color = GradientStart
            )
            Spacer(Modifier.height(8.dp))
            MacroProgressBar(
                label = "碳水",
                current = totalCarbs,
                target = targetCarbs,
                unit = "g",
                color = Color(0xFF4FC3F7)
            )
            Spacer(Modifier.height(8.dp))
            MacroProgressBar(
                label = "脂肪",
                current = totalFat,
                target = targetFat,
                unit = "g",
                color = GradientEnd
            )
        }
    }
}

@Composable
private fun MacroProgressBar(
    label: String,
    current: Float,
    target: Float,
    unit: String,
    color: Color
) {
    val progress = if (target > 0f) (current / target).coerceIn(0f, 1f) else 0f
    val percent = (progress * 100).toInt()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${current.toInt()}$unit / ${target.toInt()}$unit（$percent%）",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = SurfaceContainerHigh
        )
    }
}
