package com.csd.trainlytics.ui.workout

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WorkoutSession
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun WorkoutSummaryScreen(
    onBack: () -> Unit,
    viewModel: WorkoutSummaryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
        } else {
            val session = state.session
            if (session == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("训练记录不存在", color = Color(0xFF9B9B9B))
                }
            } else {
                WorkoutSummaryContent(session = session, onBack = onBack)
            }
        }
    }
}

@Composable
private fun WorkoutSummaryContent(
    session: WorkoutSession,
    onBack: () -> Unit
) {
    val durationMin = session.durationMillis?.let { TimeUnit.MILLISECONDS.toMinutes(it) } ?: 0L
    val totalVolume = session.totalVolumeKg.roundToInt()
    val exerciseCount = session.sets.map { it.exerciseName }.distinct().size
    val setCount = session.sets.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(Modifier.height(16.dp))
            // Done checkmark hero
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFF004820),
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    "训练完成！",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                session.templateName?.let { name ->
                    Text(name, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF9B9B9B))
                }
            }
        }

        // Stats grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryStatCard("时长", "${durationMin} 分钟", Modifier.weight(1f))
                SummaryStatCard("总重量", "$totalVolume kg", Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryStatCard("动作数", "$exerciseCount", Modifier.weight(1f))
                SummaryStatCard("总组数", "$setCount", Modifier.weight(1f))
            }
        }

        // Exercise breakdown
        if (session.sets.isNotEmpty()) {
            val groups = session.sets.groupBy { it.exerciseName }
            item {
                Text(
                    "训练详情",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                    color = Color(0xFF9B9B9B)
                )
            }
            groups.forEach { (exerciseName, sets) ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1A1A1A))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            exerciseName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.White
                        )
                        sets.forEachIndexed { i, set ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "第 ${i + 1} 组",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF9B9B9B)
                                )
                                Text(
                                    "${set.weightKg} kg × ${set.reps} 次",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (set.isPersonalRecord) Color(0xFF3FFF8B) else Color.White
                                )
                                if (set.isPersonalRecord) {
                                    Text(
                                        "PR",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF3FFF8B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79))),
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "返回首页",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF004820)
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SummaryStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF9B9B9B))
    }
}
