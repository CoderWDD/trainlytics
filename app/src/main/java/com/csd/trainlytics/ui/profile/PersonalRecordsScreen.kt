package com.csd.trainlytics.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.PersonalRecord
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

private val filterGroups = listOf(
    MuscleGroup.ALL,
    MuscleGroup.CHEST,
    MuscleGroup.BACK,
    MuscleGroup.LEGS,
    MuscleGroup.SHOULDERS,
    MuscleGroup.ARMS,
    MuscleGroup.CORE
)

@Composable
fun PersonalRecordsScreen(
    onBack: () -> Unit,
    viewModel: PersonalRecordsViewModel = hiltViewModel()
) {
    val records by viewModel.filteredRecords.collectAsState()
    val oneRMHistory by viewModel.oneRMHistoryMap.collectAsState()
    val (totalPRs, thisMonthPRs) = viewModel.headerStats.collectAsState().value
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGroup by viewModel.selectedMuscleGroup.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = "个人记录",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))

        // Header badge card
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceContainerLow
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(listOf(GradientStart.copy(alpha = 0.2f), GradientEnd.copy(alpha = 0.2f)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.EmojiEvents, null, tint = GradientStart, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "$totalPRs 个PR",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "个人最佳记录",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (thisMonthPRs > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.horizontalGradient(listOf(GradientStart, GradientEnd)))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "本月 +$thisMonthPRs",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Search bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainerHigh)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        if (searchQuery.isEmpty()) {
                            Text("搜索动作", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        }
                        inner()
                    },
                    singleLine = true
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Muscle group filter tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            items(filterGroups) { group ->
                val selected = group == selectedGroup
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (selected) Brush.horizontalGradient(listOf(GradientStart, GradientEnd))
                            else Brush.horizontalGradient(listOf(SurfaceContainerHighest, SurfaceContainerHighest))
                        )
                        .clickable { viewModel.onMuscleGroupSelected(group) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = group.displayName(),
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (records.isEmpty()) {
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Text("完成训练后，个人最佳记录将显示在这里", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(records, key = { it.exerciseId }) { record ->
                    PersonalRecordCard(
                        record = record,
                        oneRMHistory = oneRMHistory[record.exerciseId] ?: emptyList()
                    )
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
private fun PersonalRecordCard(record: PersonalRecord, oneRMHistory: List<Float>) {
    BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.exerciseName,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHighest)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = record.muscleGroup.displayName(),
                            color = GradientStart,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${"%.1f".format(record.estimatedOneRepMax)} kg",
                        color = GradientStart,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "1RM 估算",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "${"%.1f".format(record.weightKg)} kg × ${record.reps} reps",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "最佳单组",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
                if (oneRMHistory.size >= 2) {
                    OneRMSparkline(
                        values = oneRMHistory,
                        modifier = Modifier
                            .width(80.dp)
                            .height(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OneRMSparkline(values: List<Float>, modifier: Modifier = Modifier) {
    val gradientStart = GradientStart
    val gradientEnd = GradientEnd
    androidx.compose.foundation.Canvas(modifier = modifier) {
        if (values.size < 2) return@Canvas
        val minV = values.minOrNull() ?: return@Canvas
        val maxV = values.maxOrNull() ?: return@Canvas
        val range = (maxV - minV).coerceAtLeast(1f)
        val stepX = size.width / (values.size - 1)

        val path = Path()
        values.forEachIndexed { i, v ->
            val x = i * stepX
            val y = size.height - ((v - minV) / range) * size.height
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(gradientStart, gradientEnd),
                startX = 0f,
                endX = size.width
            ),
            style = Stroke(width = 2.dp.toPx())
        )

        // Dot at latest point
        val lastX = (values.size - 1) * stepX
        val lastV = values.last()
        val lastY = size.height - ((lastV - minV) / range) * size.height
        drawCircle(color = gradientEnd, radius = 3.dp.toPx(), center = Offset(lastX, lastY))
    }
}
