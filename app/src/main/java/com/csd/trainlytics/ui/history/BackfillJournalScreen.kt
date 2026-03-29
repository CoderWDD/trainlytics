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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import com.csd.trainlytics.ui.theme.WarningAmber
import java.time.format.DateTimeFormatter

@Composable
fun BackfillJournalScreen(
    onBack: () -> Unit,
    onNavigateToStartWorkout: () -> Unit = {},
    onNavigateToRecordBody: () -> Unit = {},
    onNavigateToRecordMeal: () -> Unit = {},
    viewModel: BackfillViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GradientStart)
        }
        return
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    text = "补记日志",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Hero card
        item {
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Column {
                    val incompleteCount = state.incompleteDays.size
                    Text(
                        text = if (incompleteCount == 0) "本周日志全部完整！" else "本周有 $incompleteCount 天日志不完整",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (incompleteCount == 0) GradientStart else WarningAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "完成度 ${state.completedDays}/${state.totalDays} 天",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (state.streakDays > 0) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(GradientStart.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.EmojiEvents,
                                    contentDescription = null,
                                    tint = GradientStart,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "连续 ${state.streakDays} 天",
                                    fontSize = 12.sp,
                                    color = GradientStart,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { state.completedDays.toFloat() / state.totalDays },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GradientStart,
                        trackColor = SurfaceContainerHigh
                    )
                }
            }
        }

        // Gap day cards
        if (state.incompleteDays.isEmpty()) {
            item {
                BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                    Text(
                        text = "所有记录都完整，继续保持！",
                        color = GradientStart,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            items(state.incompleteDays) { gap ->
                GapDayCard(
                    gap = gap,
                    onNavigateToStartWorkout = onNavigateToStartWorkout,
                    onNavigateToRecordBody = onNavigateToRecordBody,
                    onNavigateToRecordMeal = onNavigateToRecordMeal
                )
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun GapDayCard(
    gap: DayGap,
    onNavigateToStartWorkout: () -> Unit,
    onNavigateToRecordBody: () -> Unit,
    onNavigateToRecordMeal: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("M月d日 EEE")
    val missingLabels = buildList {
        if (gap.missingWorkout) add("训练")
        if (gap.missingWeight) add("体重")
        if (gap.missingMeal) add("餐食")
    }

    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = gap.date.format(dateFormatter),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "缺 ${missingLabels.joinToString("+")}",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarningAmber
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (gap.missingWorkout) {
                    GapActionChip(
                        icon = Icons.Filled.FitnessCenter,
                        label = "补记训练",
                        onClick = onNavigateToStartWorkout
                    )
                }
                if (gap.missingWeight) {
                    GapActionChip(
                        icon = Icons.Filled.MonitorWeight,
                        label = "补记体重",
                        onClick = onNavigateToRecordBody
                    )
                }
                if (gap.missingMeal) {
                    GapActionChip(
                        icon = Icons.Filled.Restaurant,
                        label = "补记餐食",
                        onClick = onNavigateToRecordMeal
                    )
                }
            }
        }
    }
}

@Composable
private fun GapActionChip(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceContainerHighest)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = GradientStart, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp, color = GradientStart, fontWeight = FontWeight.Medium)
    }
}
