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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.DaySummary
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.model.WorkoutSession
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val detailDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd EEE")

@Composable
fun HistoryDayDetailScreen(
    onBack: () -> Unit,
    viewModel: HistoryDayDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = Color.White)
            }
            if (state.day != null) {
                val date = Instant.ofEpochMilli(state.day!!.dateMillis)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
                Text(
                    text = date.format(detailDateFormatter),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
            state.day == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("数据不存在", color = Color(0xFF9B9B9B))
            }
            else -> DayDetailContent(day = state.day!!)
        }
    }
}

@Composable
private fun DayDetailContent(day: DaySummary) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(Modifier.height(4.dp)) }

        // Body stats section
        if (day.bodyRecord != null) {
            item {
                SectionHeader("身体数据")
            }
            item {
                val br = day.bodyRecord
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1A1A1A))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    br.weightKg?.let { StatRow("体重", "${it} kg") }
                    br.bodyFatPercent?.let { StatRow("体脂率", "${it}%") }
                    br.waistCm?.let { StatRow("腰围", "${it} cm") }
                    if (br.note.isNotBlank()) {
                        Text(br.note, style = MaterialTheme.typography.bodySmall, color = Color(0xFF9B9B9B))
                    }
                }
            }
        }

        // Meals section
        if (day.hasMeals) {
            item { SectionHeader("饮食记录") }
            val grouped = day.meals.groupBy { it.mealType }
            val mealTypeOrder = listOf(MealType.BREAKFAST, MealType.LUNCH, MealType.DINNER, MealType.SNACK)
            items(mealTypeOrder.filter { grouped.containsKey(it) }) { mealType ->
                val meals = grouped[mealType] ?: emptyList()
                MealTypeCard(mealType = mealType, meals = meals)
            }
        }

        // Workout section
        if (day.hasWorkout) {
            val completedSessions = day.sessions.filter { it.isCompleted }
            if (completedSessions.isNotEmpty()) {
                item { SectionHeader("训练记录") }
                items(completedSessions) { session ->
                    WorkoutSessionCard(session = session)
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
        color = Color(0xFF9B9B9B),
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF9B9B9B))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = Color.White)
    }
}

private val mealTypeLabels = mapOf(
    MealType.BREAKFAST to "早餐",
    MealType.LUNCH to "午餐",
    MealType.DINNER to "晚餐",
    MealType.SNACK to "加餐"
)

@Composable
private fun MealTypeCard(mealType: MealType, meals: List<MealRecord>) {
    val totalCals = meals.sumOf { it.calories.toDouble() }.roundToInt()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                mealTypeLabels[mealType] ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Text(
                "${totalCals} kcal",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF3FFF8B)
            )
        }
        meals.forEach { meal ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    meal.foodName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFCCCCCC),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "${meal.calories.roundToInt()} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9B9B9B)
                )
            }
        }
    }
}

@Composable
private fun WorkoutSessionCard(session: WorkoutSession) {
    val exerciseCount = session.sets.map { it.exerciseName }.distinct().size
    val setCount = session.sets.size
    val totalVolume = session.totalVolumeKg.roundToInt()
    val durationMin = session.durationMillis?.let { java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(it) } ?: 0L

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        session.templateName?.let { name ->
            Text(
                name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WorkoutStatChip("${durationMin} 分钟", Modifier.weight(1f))
            WorkoutStatChip("${totalVolume} kg", Modifier.weight(1f))
            WorkoutStatChip("$exerciseCount 动作", Modifier.weight(1f))
            WorkoutStatChip("$setCount 组", Modifier.weight(1f))
        }
        if (session.sets.isNotEmpty()) {
            val groups = session.sets.groupBy { it.exerciseName }
            groups.forEach { (exerciseName, sets) ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        exerciseName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFFCCCCCC)
                    )
                    sets.forEachIndexed { i, set ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "第 ${i + 1} 组",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B6B6B)
                            )
                            Text(
                                "${set.weightKg} kg × ${set.reps} 次${if (set.isPersonalRecord) " 🏆" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (set.isPersonalRecord) Color(0xFF3FFF8B) else Color(0xFF9B9B9B)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutStatChip(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF222222))
            .padding(horizontal = 6.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFCCCCCC)
        )
    }
}
