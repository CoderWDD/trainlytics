package com.csd.trainlytics.ui.workout

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutSummaryScreen(
    sessionId: Long,
    onNavigateHome: () -> Unit,
    onNavigateToDetail: ((String) -> Unit)? = null,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val sessionState by remember { viewModel.getSessionWithSets(sessionId) }.collectAsState()
    var fatigueRating by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        // Celebration header
        CelebrationHeader(session = sessionState)

        Spacer(Modifier.height(24.dp))

        // Bento stats grid
        sessionState?.let { s ->
            StatsGrid(session = s)
            Spacer(Modifier.height(16.dp))

            // PR banner if there's an estimated 1RM
            val topPr = s.sets
                .filter { it.isCompleted && it.estimatedOneRepMax != null }
                .maxByOrNull { it.estimatedOneRepMax!! }
            if (topPr != null) {
                PrBanner(exerciseName = topPr.exerciseName, oneRm = topPr.estimatedOneRepMax!!)
                Spacer(Modifier.height(16.dp))
            }
        }

        // Fatigue rating
        FatigueRatingCard(
            rating = fatigueRating,
            onRatingChange = { fatigueRating = it }
        )

        Spacer(Modifier.height(24.dp))

        // Action buttons
        GradientButton(
            text = "返回今日",
            onClick = {
                viewModel.completeSession(sessionId, fatigueRating.takeIf { it > 0 }) {
                    onNavigateHome()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (onNavigateToDetail != null) {
            Spacer(Modifier.height(12.dp))
            val session = sessionState?.session
            val dateStr = session?.date?.toString() ?: ""
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainerHigh)
                    .clickable { onNavigateToDetail(dateStr) }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "查看详情",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun CelebrationHeader(session: WorkoutSessionWithSets?) {
    Column {
        Text(
            text = "训练完成！",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = GradientStart
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = session?.session?.name ?: "今日训练",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatsGrid(session: WorkoutSessionWithSets) {
    val durationMinutes = session.session.durationMinutes
    val durationText = if (durationMinutes != null) {
        val h = durationMinutes / 60
        val m = durationMinutes % 60
        if (h > 0) "${h}h ${m}m" else "${m}m"
    } else "进行中"

    val totalVolume = session.totalVolume
    val volumeText = if (totalVolume >= 1000f) {
        "%.1fk kg".format(totalVolume / 1000f)
    } else {
        "%.0f kg".format(totalVolume)
    }

    val setsCount = session.completedSetsCount
    val exerciseCount = session.exerciseCount

    val topOneRm = session.sets
        .filter { it.isCompleted && it.estimatedOneRepMax != null }
        .maxOfOrNull { it.estimatedOneRepMax!! }
    val oneRmText = if (topOneRm != null) "%.1f kg".format(topOneRm) else "—"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(label = "训练时长", value = durationText, modifier = Modifier.weight(1f))
        StatCard(label = "总训练量", value = volumeText, modifier = Modifier.weight(1f))
    }
    Spacer(Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(label = "完成组数", value = "${setsCount} 组", modifier = Modifier.weight(1f))
        StatCard(label = "最高估算1RM", value = oneRmText, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    BentoCard(
        modifier = modifier,
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
private fun PrBanner(exerciseName: String, oneRm: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFD700).copy(alpha = 0.25f), Color(0xFFFFA500).copy(alpha = 0.15f))
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.EmojiEvents,
            contentDescription = "PR",
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = "新个人记录",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$exerciseName 估算 1RM: %.1f kg".format(oneRm),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun FatigueRatingCard(rating: Int, onRatingChange: (Int) -> Unit) {
    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Text(
                text = "疲劳度评估",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "本次训练后的疲劳感受",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "$i 星",
                        tint = if (i <= rating) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onRatingChange(i) }
                    )
                }
            }
            if (rating > 0) {
                Spacer(Modifier.height(8.dp))
                val label = when (rating) {
                    1 -> "状态极佳，毫不疲惫"
                    2 -> "轻微疲劳，状态不错"
                    3 -> "适度疲劳，正常水平"
                    4 -> "较为疲劳，需要恢复"
                    5 -> "非常疲惫，充分休息"
                    else -> ""
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
