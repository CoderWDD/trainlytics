package com.csd.trainlytics.ui.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WeeklyReviewData
import com.csd.trainlytics.domain.model.WeightTrendPoint
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientProgressBar
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import com.csd.trainlytics.ui.theme.WarningAmber
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer

@Composable
fun InsightsDashboardScreen(
    onNavigateToWeeklyReview: (String) -> Unit,
    onNavigateToWeeklyComparison: () -> Unit,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GradientStart)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "洞察",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        // Weekly performance card
        val currentWeek = state.multiWeekComparison?.currentWeek
        if (currentWeek != null) {
            WeeklyPerformanceCard(
                data = currentWeek,
                onNavigate = { onNavigateToWeeklyReview(currentWeek.weekStart.toString()) }
            )
            Spacer(Modifier.height(12.dp))
        }

        // Weight trend card
        if (state.weightTrend.isNotEmpty()) {
            WeightTrendCard(points = state.weightTrend)
            Spacer(Modifier.height(12.dp))
        }

        // Multi-week comparison
        val comparison = state.multiWeekComparison
        if (comparison?.previousWeek != null) {
            MultiWeekCard(
                currentScore = comparison.currentWeek?.performanceScore ?: 0,
                prevScore = comparison.previousWeek?.performanceScore ?: 0,
                twoAgoScore = comparison.twoWeeksAgo?.performanceScore ?: 0,
                onNavigate = onNavigateToWeeklyComparison
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun WeeklyPerformanceCard(
    data: WeeklyReviewData,
    onNavigate: () -> Unit
) {
    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "本周表现",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "查看详情 →",
                    style = MaterialTheme.typography.labelMedium,
                    color = GradientStart
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "训练次数", value = "${data.workoutCount}次")
                StatItem(label = "平均热量", value = "${data.avgDailyCalories.toInt()} kcal")
                StatItem(label = "平均蛋白", value = "${data.avgDailyProteinG.toInt()}g")
                StatItem(label = "综合评分", value = "${data.performanceScore}")
            }
            Spacer(Modifier.height(12.dp))
            GradientProgressBar(
                progress = data.performanceScore / 100f
            )
        }
    }
}

@Composable
private fun WeightTrendCard(points: List<WeightTrendPoint>) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            modelProducer.runTransaction {
                lineSeries {
                    // Raw weight line
                    series(
                        x = points.indices.map { it },
                        y = points.map { it.weightKg }
                    )
                    // 7-day moving average line (if available)
                    val movingAvgPoints = points.filter { it.movingAvg7Day != null }
                    if (movingAvgPoints.isNotEmpty()) {
                        val firstAvgIndex = points.indexOfFirst { it.movingAvg7Day != null }
                        series(
                            x = movingAvgPoints.indices.map { it + firstAvgIndex },
                            y = movingAvgPoints.map { it.movingAvg7Day!! }
                        )
                    }
                }
            }
        }
    }

    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Text(
                text = "体重趋势",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            val latest = points.lastOrNull()
            val earliest = points.firstOrNull()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "当前体重",
                    value = latest?.let { "${"%.1f".format(it.weightKg)} kg" } ?: "--"
                )
                val change = if (latest != null && earliest != null)
                    latest.weightKg - earliest.weightKg else null
                StatItem(
                    label = "近90天变化",
                    value = change?.let { "${if (it >= 0) "+" else ""}${"%.1f".format(it)} kg" } ?: "--"
                )
            }
            Spacer(Modifier.height(12.dp))
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(
                        lineProvider = LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(GradientStart))
                            ),
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(GradientEnd))
                            )
                        )
                    ),
                    startAxis = VerticalAxis.rememberStart()
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }
    }
}

@Composable
private fun MultiWeekCard(
    currentScore: Int,
    prevScore: Int,
    twoAgoScore: Int,
    onNavigate: () -> Unit
) {
    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerHigh
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "多周对比",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "详情 →",
                    style = MaterialTheme.typography.labelMedium,
                    color = GradientStart
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeekScoreBar(label = "两周前", score = twoAgoScore, maxScore = 100)
                WeekScoreBar(label = "上周", score = prevScore, maxScore = 100)
                WeekScoreBar(label = "本周", score = currentScore, maxScore = 100, highlight = true)
            }
        }
    }
}

@Composable
private fun WeekScoreBar(
    label: String,
    score: Int,
    maxScore: Int,
    highlight: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$score",
            style = MaterialTheme.typography.headlineSmall,
            color = if (highlight) GradientStart else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}
