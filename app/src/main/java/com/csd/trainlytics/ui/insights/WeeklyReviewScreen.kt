package com.csd.trainlytics.ui.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WeeklyReviewData
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientProgressBar
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import java.time.format.DateTimeFormatter

@Composable
fun WeeklyReviewScreen(
    onBack: () -> Unit,
    viewModel: WeeklyReviewViewModel = hiltViewModel()
) {
    val review by viewModel.reviewData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = "本周回顾",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(16.dp))

        if (review == null) {
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Text(
                    text = "本周数据收集中，请先记录训练和饮食",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            review?.let { r ->
                WeekRangeHeader(r)
                Spacer(Modifier.height(16.dp))
                ScoreCard(score = r.performanceScore)
                Spacer(Modifier.height(12.dp))
                NutritionReviewCard(r)
                Spacer(Modifier.height(12.dp))
                WorkoutReviewCard(r)
                Spacer(Modifier.height(12.dp))
                if (r.weightChange != null) {
                    WeightChangeCard(r)
                }
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun WeekRangeHeader(r: WeeklyReviewData) {
    val fmt = DateTimeFormatter.ofPattern("M月d日")
    Text(
        text = "${r.weekStart.format(fmt)} — ${r.weekEnd.format(fmt)}",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 14.sp
    )
}

@Composable
private fun ScoreCard(score: Int) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "综合评分",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.displaySmall,
                    color = GradientStart,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = when {
                    score >= 85 -> "优秀"
                    score >= 70 -> "良好"
                    score >= 55 -> "一般"
                    else -> "需努力"
                },
                style = MaterialTheme.typography.titleMedium,
                color = GradientStart,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun NutritionReviewCard(r: WeeklyReviewData) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Text("饮食", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("日均热量", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text("${r.avgDailyCalories.toInt()} kcal", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("日均蛋白质", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text("${r.avgDailyProteinG.toInt()} g", color = GradientStart, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("蛋白目标达标天", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text("${r.proteinGoalDaysAchieved}/7 天", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun WorkoutReviewCard(r: WeeklyReviewData) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Text("训练", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("训练次数", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text("${r.workoutCount} 次", color = GradientStart, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("累计训练量", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text("${r.totalVolumeKg.toInt()} kg", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun WeightChangeCard(r: WeeklyReviewData) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Text("体重变化", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("均重", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text("${r.avgWeightKg?.let { "%.1f".format(it) } ?: "—"} kg", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
            }
            Spacer(Modifier.height(4.dp))
            val change = r.weightChange ?: 0f
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("变化", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                val sign = if (change >= 0) "+" else ""
                Text("$sign${"%.1f".format(change)} kg", color = GradientStart, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}
