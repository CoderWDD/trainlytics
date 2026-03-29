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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HistoryListScreen(
    onNavigateToDayDetail: (String) -> Unit,
    onNavigateToBackfill: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val today = LocalDate.now()
    val weekDays = (6 downTo 0).map { today.minusDays(it.toLong()) }
    val sessionsByDate = state.sessions.groupBy { it.date }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "训练记录",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerHigh)
                    .clickable { onNavigateToBackfill() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.EditCalendar,
                    contentDescription = "补记日志",
                    tint = GradientStart,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "补记日志",
                    color = GradientStart,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        // Weekly calendar strip
        WeeklyCalendarStrip(
            days = weekDays,
            selectedDate = state.selectedDate,
            activeDates = sessionsByDate.keys,
            onSelectDate = { viewModel.selectDate(it) }
        )

        Spacer(Modifier.height(16.dp))

        // Session list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredSessions = state.sessions
                .filter { it.date == state.selectedDate }
                .sortedByDescending { it.startTime }

            if (filteredSessions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "当天无训练记录",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(filteredSessions, key = { it.id }) { session ->
                    SessionCard(
                        session = session,
                        onClick = { onNavigateToDayDetail(state.selectedDate.toString()) }
                    )
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun WeeklyCalendarStrip(
    days: List<LocalDate>,
    selectedDate: LocalDate,
    activeDates: Set<LocalDate>,
    onSelectDate: (LocalDate) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            val isSelected = day == selectedDate
            val hasActivity = activeDates.contains(day)
            val isToday = day == LocalDate.now()
            val dayFormatter = DateTimeFormatter.ofPattern("d")
            val weekFormatter = DateTimeFormatter.ofPattern("EE")

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            isSelected -> Brush.verticalGradient(listOf(GradientStart, GradientEnd))
                            else -> Brush.verticalGradient(listOf(SurfaceContainerHigh, SurfaceContainerHigh))
                        }
                    )
                    .clickable { onSelectDate(day) }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = day.format(weekFormatter),
                    fontSize = 11.sp,
                    color = if (isSelected) SurfaceContainerLow else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = day.format(dayFormatter),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) SurfaceContainerLow else MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                // Activity dot
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> SurfaceContainerLow
                                hasActivity -> GradientStart
                                else -> SurfaceContainerHighest
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun SessionCard(
    session: WorkoutSession,
    onClick: () -> Unit
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        backgroundColor = SurfaceContainerLow
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GradientStart.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.FitnessCenter,
                    contentDescription = null,
                    tint = GradientStart,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = buildString {
                        append(session.startTime.format(timeFormatter))
                        session.durationMinutes?.let { append(" · ${it}分钟") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
