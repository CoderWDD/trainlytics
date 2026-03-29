package com.csd.trainlytics.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WeeklyReviewData
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import java.time.format.DateTimeFormatter

@Composable
fun WeeklyComparisonScreen(
    onBack: () -> Unit,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val comparison = state.multiWeekComparison

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
                text = "多周对比",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(16.dp))

        if (comparison == null) {
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Text("暂无对比数据", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            // Score bars comparison
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Column {
                    Text("综合评分对比", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    listOf(
                        "两周前" to comparison.twoWeeksAgo,
                        "上周" to comparison.previousWeek,
                        "本周" to comparison.currentWeek
                    ).forEach { (label, week) ->
                        WeekScoreBar(label = label, review = week)
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Stats table
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Column {
                    Text("指标对比", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    ComparisonRow(
                        label = "训练次数",
                        twoWeeks = comparison.twoWeeksAgo?.workoutCount?.toString() ?: "—",
                        lastWeek = comparison.previousWeek?.workoutCount?.toString() ?: "—",
                        thisWeek = comparison.currentWeek?.workoutCount?.toString() ?: "—"
                    )
                    ComparisonRow(
                        label = "日均热量",
                        twoWeeks = comparison.twoWeeksAgo?.avgDailyCalories?.toInt()?.toString() ?: "—",
                        lastWeek = comparison.previousWeek?.avgDailyCalories?.toInt()?.toString() ?: "—",
                        thisWeek = comparison.currentWeek?.avgDailyCalories?.toInt()?.toString() ?: "—"
                    )
                    ComparisonRow(
                        label = "日均蛋白(g)",
                        twoWeeks = comparison.twoWeeksAgo?.avgDailyProteinG?.toInt()?.toString() ?: "—",
                        lastWeek = comparison.previousWeek?.avgDailyProteinG?.toInt()?.toString() ?: "—",
                        thisWeek = comparison.currentWeek?.avgDailyProteinG?.toInt()?.toString() ?: "—"
                    )
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun WeekScoreBar(label: String, review: WeeklyReviewData?) {
    val score = review?.performanceScore ?: 0
    val fraction = (score / 100f).coerceIn(0f, 1f)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            modifier = Modifier.width(48.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(SurfaceContainerHigh)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Brush.horizontalGradient(listOf(GradientStart, GradientEnd)))
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (score > 0) "$score" else "—",
            color = if (score > 0) GradientStart else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.width(32.dp)
        )
    }
}

@Composable
private fun ComparisonRow(label: String, twoWeeks: String, lastWeek: String, thisWeek: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
        Text(twoWeeks, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(lastWeek, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(thisWeek, color = GradientStart, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, modifier = Modifier.weight(1f))
    }
}
