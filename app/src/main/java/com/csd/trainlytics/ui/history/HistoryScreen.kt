package com.csd.trainlytics.ui.history

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.csd.trainlytics.domain.model.DayStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val dayFormatter = DateTimeFormatter.ofPattern("MM/dd")
private val weekdayFormatter = DateTimeFormatter.ofPattern("EEE")

@Composable
fun HistoryScreen(
    onNavigateToDayDetail: (Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "历史记录",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
        } else if (state.days.all { it.completenessStatus == DayStatus.EMPTY }) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("暂无记录", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF9B9B9B))
                    Spacer(Modifier.height(8.dp))
                    Text("开始记录你的第一天", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF6B6B6B))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { Spacer(Modifier.height(4.dp)) }
                items(state.days.filter { it.completenessStatus != DayStatus.EMPTY }) { day ->
                    DayHistoryCard(
                        day = day,
                        onClick = { onNavigateToDayDetail(day.dateMillis) }
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun DayHistoryCard(day: DaySummary, onClick: () -> Unit) {
    val zone = ZoneId.systemDefault()
    val date = Instant.ofEpochMilli(day.dateMillis).atZone(zone).toLocalDate()
    val isToday = date == LocalDate.now()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Date badge
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isToday) Color(0xFF1E3320) else Color(0xFF222222))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.format(dayFormatter),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isToday) Color(0xFF3FFF8B) else Color.White
            )
            Text(
                text = date.format(weekdayFormatter),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9B9B9B)
            )
        }

        // Day data summary
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (day.bodyRecord?.weightKg != null) {
                Text(
                    "体重 ${day.bodyRecord.weightKg} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
            if (day.hasMeals) {
                val totalCals = day.meals.sumOf { it.calories.toDouble() }.roundToInt()
                Text(
                    "热量 ${totalCals} kcal · ${day.meals.size} 餐",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9B9B9B)
                )
            }
            if (day.hasWorkout) {
                val sessions = day.sessions.filter { it.isCompleted }
                if (sessions.isNotEmpty()) {
                    val vol = sessions.sumOf { it.totalVolumeKg.toDouble() }.roundToInt()
                    Text(
                        "训练 ${sessions.size} 次 · ${vol} kg",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9B9B9B)
                    )
                }
            }
        }

        // Status dot
        val dotColor = when (day.completenessStatus) {
            DayStatus.COMPLETE -> Color(0xFF3FFF8B)
            DayStatus.PARTIAL -> Color(0xFFFACC15)
            DayStatus.EMPTY -> Color(0xFF3A3A3A)
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
    }
}
