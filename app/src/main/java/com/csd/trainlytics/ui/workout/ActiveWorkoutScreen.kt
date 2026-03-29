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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun ActiveWorkoutScreen(
    sessionId: Long,
    onComplete: (Long) -> Unit,
    onNavigateToExercisePicker: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val activeSession = state.activeSession
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = activeSession?.session?.name ?: "训练中",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Timer, null, tint = GradientStart, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    val hours = elapsedSeconds / 3600
                    val minutes = (elapsedSeconds % 3600) / 60
                    val seconds = elapsedSeconds % 60
                    Text(
                        text = "%02d:%02d:%02d".format(hours, minutes, seconds),
                        color = GradientStart,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
            GradientButton(
                text = "完成",
                onClick = {
                    viewModel.completeSession(sessionId, null) { onComplete(sessionId) }
                },
                modifier = Modifier.width(100.dp),
                height = 40.dp,
                cornerRadius = 20.dp
            )
        }

        Spacer(Modifier.height(16.dp))

        // Stats row
        if (activeSession != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(label = "动作", value = "${activeSession.exerciseCount}", modifier = Modifier.weight(1f))
                StatChip(label = "组数", value = "${activeSession.completedSetsCount}", modifier = Modifier.weight(1f))
                StatChip(label = "总量", value = "${activeSession.totalVolume.toInt()} kg", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            // Training insights Bento card
            val rpeValues = activeSession.sets.mapNotNull { it.rpe }
            val avgRpe = if (rpeValues.isNotEmpty()) rpeValues.average() else null
            BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GradientStart.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Insights, null, tint = GradientStart, modifier = Modifier.size(20.dp))
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "${activeSession.totalVolume.toInt()} kg",
                                color = GradientStart,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "预估总量",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                        if (avgRpe != null) {
                            Column {
                                Text(
                                    text = "%.1f".format(avgRpe),
                                    color = GradientStart,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "平均 RPE",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Sets list grouped by exercise
        val setsByExercise = activeSession?.sets?.groupBy { it.exerciseId } ?: emptyMap()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            setsByExercise.forEach { (exerciseId, sets) ->
                item {
                    ExerciseSetGroup(
                        exerciseName = sets.firstOrNull()?.exerciseName ?: "未知动作",
                        sets = sets,
                        onAddSet = { weight, reps, rpe ->
                            viewModel.addSet(
                                WorkoutSet(
                                    sessionId = sessionId,
                                    exerciseId = exerciseId,
                                    exerciseName = sets.firstOrNull()?.exerciseName ?: "",
                                    setNumber = sets.size + 1,
                                    weightKg = weight,
                                    reps = reps,
                                    rpe = rpe,
                                    isCompleted = true,
                                    timestamp = LocalDateTime.now()
                                )
                            )
                        }
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(GradientStart.copy(alpha = 0.1f))
                        .clickable { onNavigateToExercisePicker() }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, null, tint = GradientStart)
                        Spacer(Modifier.width(8.dp))
                        Text("添加动作", color = GradientStart, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun ExerciseSetGroup(
    exerciseName: String,
    sets: List<WorkoutSet>,
    onAddSet: (Float?, Int?, Float?) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var repsText by remember { mutableStateOf("") }
    var rpeText by remember { mutableStateOf("") }

    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow
    ) {
        Column {
            Text(
                text = exerciseName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            // Existing sets
            sets.forEach { set ->
                SetRow(set = set)
                Spacer(Modifier.height(4.dp))
            }

            Spacer(Modifier.height(8.dp))

            // Add set row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("重量 kg") },
                    modifier = Modifier.weight(2f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        cursorColor = GradientStart
                    )
                )
                OutlinedTextField(
                    value = repsText,
                    onValueChange = { repsText = it },
                    label = { Text("次数") },
                    modifier = Modifier.weight(2f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        cursorColor = GradientStart
                    )
                )
                OutlinedTextField(
                    value = rpeText,
                    onValueChange = { rpeText = it },
                    label = { Text("RPE") },
                    modifier = Modifier.weight(1.5f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        cursorColor = GradientStart
                    )
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                        .clickable {
                            onAddSet(weightText.toFloatOrNull(), repsText.toIntOrNull(), rpeText.toFloatOrNull())
                            weightText = ""
                            repsText = ""
                            rpeText = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, null, tint = androidx.compose.ui.graphics.Color(0xFF00210B), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun SetRow(set: WorkoutSet) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainerHighest)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "组 ${set.setNumber}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
        Text(
            text = buildString {
                set.weightKg?.let { append("${it}kg") }
                if (set.weightKg != null && set.reps != null) append(" × ")
                set.reps?.let { append("${it}次") }
                set.rpe?.let { append(" RPE$it") }
            },
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Icon(
            Icons.Filled.Check,
            null,
            tint = if (set.isCompleted) GradientStart else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    BentoCard(
        modifier = modifier,
        backgroundColor = SurfaceContainerHigh,
        padding = 10.dp
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
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
}
