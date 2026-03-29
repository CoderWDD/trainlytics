package com.csd.trainlytics.ui.profile

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
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientProgressBar
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToPersonalRecords: () -> Unit,
    onNavigateToExportData: () -> Unit,
    onNavigateToImportData: () -> Unit,
    onNavigateToGoalSetup: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
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
            text = "我的",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))

        // Goal summary card
        val goal = state.goal
        if (goal != null) {
            GoalSummaryCard(
                phase = goal.fitnessPhase.displayName(),
                currentWeight = goal.currentWeightKg,
                targetWeight = goal.targetWeightKg,
                onEdit = onNavigateToGoalSetup
            )
        } else {
            SetGoalCard(onClick = onNavigateToGoalSetup)
        }

        Spacer(Modifier.height(20.dp))

        // Goal-derived stats grid
        if (goal != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(label = "目标热量", value = "${goal.targetCalories.toInt()} kcal", modifier = Modifier.weight(1f))
                ProfileStatCard(label = "目标蛋白质", value = "${goal.targetProteinG.toInt()} g", modifier = Modifier.weight(1f))
                ProfileStatCard(label = "训练天/周", value = "${goal.weeklyWorkoutDays}", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
        }

        // Usage stats Bento grid
        val stats = state.usageStats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(label = "连续记录", value = "${stats.streakDays} 天", modifier = Modifier.weight(1f))
            ProfileStatCard(label = "累计记录", value = "${stats.totalRecordedDays} 天", modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(label = "总训练次数", value = "${stats.totalWorkouts} 次", modifier = Modifier.weight(1f))
            val volumeDisplay = if (stats.totalVolumeKg >= 1000f) {
                "${"%.1f".format(stats.totalVolumeKg / 1000f)} t"
            } else {
                "${stats.totalVolumeKg.toInt()} kg"
            }
            ProfileStatCard(label = "累计训练量", value = volumeDisplay, modifier = Modifier.weight(1f))
        }

        // Latest body data row
        state.latestBodyRecord?.let { body ->
            Spacer(Modifier.height(12.dp))
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    body.weightKg?.let { weight ->
                        BodyStatItem(label = "最近体重", value = "${"%.1f".format(weight)} kg", modifier = Modifier.weight(1f))
                    }
                    body.bodyFatPercent?.let { bf ->
                        BodyStatItem(label = "体脂率", value = "${"%.1f".format(bf)}%", modifier = Modifier.weight(1f))
                    }
                    goal?.heightCm?.let { h ->
                        body.weightKg?.let { w ->
                            val bmi = w / ((h / 100f) * (h / 100f))
                            BodyStatItem(label = "BMI", value = "${"%.1f".format(bmi)}", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Settings menu
        Text(
            text = "功能",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))

        MenuCard {
            MenuRow(
                icon = Icons.Filled.EmojiEvents,
                label = "个人记录",
                onClick = onNavigateToPersonalRecords
            )
            MenuDivider()
            MenuRow(
                icon = Icons.Filled.Settings,
                label = "设置",
                onClick = onNavigateToSettings
            )
            MenuDivider()
            MenuRow(
                icon = Icons.Filled.Notifications,
                label = "通知设置",
                onClick = onNavigateToNotifications
            )
            MenuDivider()
            MenuRow(
                icon = Icons.Filled.Backup,
                label = "导出数据",
                onClick = onNavigateToExportData
            )
            MenuDivider()
            MenuRow(
                icon = Icons.Filled.Download,
                label = "导入数据",
                onClick = onNavigateToImportData
            )
        }

        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun GoalSummaryCard(
    phase: String,
    currentWeight: Float?,
    targetWeight: Float?,
    onEdit: () -> Unit
) {
    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(GradientStart.copy(alpha = 0.2f), GradientEnd.copy(alpha = 0.2f))
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(text = phase, color = GradientStart, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Text(text = "编辑目标 →", color = GradientStart, fontSize = 13.sp)
            }
            Spacer(Modifier.height(12.dp))
            if (currentWeight != null && targetWeight != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "当前体重", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text(
                            text = "$currentWeight kg",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "目标体重", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text(
                            text = "$targetWeight kg",
                            style = MaterialTheme.typography.titleLarge,
                            color = GradientStart,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                val progress = if (currentWeight > targetWeight) {
                    val diff = currentWeight - targetWeight
                    val total = currentWeight - targetWeight + 1
                    (total - diff) / total
                } else 0.8f
                GradientProgressBar(progress = progress.coerceIn(0f, 1f))
            }
        }
    }
}

@Composable
private fun SetGoalCard(onClick: () -> Unit) {
    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        backgroundColor = SurfaceContainerHigh
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "设置健身目标",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "告诉我们你的目标，获得个性化计划 →",
                style = MaterialTheme.typography.bodySmall,
                color = GradientStart
            )
        }
    }
}

@Composable
private fun ProfileStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    BentoCard(
        modifier = modifier,
        backgroundColor = SurfaceContainerHighest,
        padding = 12.dp
    ) {
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun MenuCard(content: @Composable () -> Unit) {
    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        padding = 0.dp
    ) {
        Column { content() }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHighest),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GradientStart,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun MenuDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 16.dp)
            .background(SurfaceContainerHighest)
    )
}

@Composable
private fun BodyStatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = GradientStart,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}
