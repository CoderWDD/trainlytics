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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.WorkoutSet
import kotlin.math.roundToInt

private val muscleGroupLabels = mapOf(
    MuscleGroup.ALL to "全部",
    MuscleGroup.CHEST to "胸",
    MuscleGroup.BACK to "背",
    MuscleGroup.LEGS to "腿",
    MuscleGroup.SHOULDERS to "肩",
    MuscleGroup.ARMS to "手臂",
    MuscleGroup.CORE to "核心"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    onBack: () -> Unit,
    onWorkoutFinished: (Long) -> Unit,
    viewModel: ActiveWorkoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.finished, state.finishedSessionId) {
        if (state.finished && state.finishedSessionId != null) {
            onWorkoutFinished(state.finishedSessionId!!)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
            Text(
                text = "训练中",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White,
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
            if (state.isFinishing) {
                CircularProgressIndicator(
                    color = Color(0xFF3FFF8B),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Button(
                    onClick = viewModel::finishWorkout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3FFF8B)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("完成", color = Color(0xFF004820), fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
        } else {
            val session = state.session
            val sets = session?.sets ?: emptyList()
            val exerciseGroups = sets.groupBy { it.exerciseName }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }

                // Stats summary
                item {
                    WorkoutStatsRow(sets = sets)
                }

                // Exercise groups
                exerciseGroups.forEach { (exerciseName, exerciseSets) ->
                    item {
                        ExerciseGroupCard(
                            exerciseName = exerciseName,
                            sets = exerciseSets
                        )
                    }
                }

                if (exerciseGroups.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "点击下方「添加动作」开始记录",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6B6B6B)
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(8.dp)) }
            }

            // Add exercise button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = viewModel::openExercisePicker,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color(0xFF3FFF8B),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("添加动作", color = Color(0xFF3FFF8B), fontWeight = FontWeight.Medium)
                }
            }
        }
    }

    // Exercise picker sheet
    if (state.showExercisePicker) {
        ModalBottomSheet(
            onDismissRequest = viewModel::dismissExercisePicker,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF141414),
            tonalElevation = 0.dp
        ) {
            ExercisePickerContent(
                exercises = state.exercises,
                query = state.exerciseQuery,
                selectedGroup = state.selectedMuscleGroup,
                onQueryChange = viewModel::onExerciseQueryChange,
                onGroupChange = viewModel::onMuscleGroupChange,
                onSelect = viewModel::selectExercise
            )
        }
    }

    // Set entry sheet
    if (state.showSetEntry && state.pendingExercise != null) {
        ModalBottomSheet(
            onDismissRequest = viewModel::dismissSetEntry,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF141414),
            tonalElevation = 0.dp
        ) {
            SetEntryContent(
                exerciseName = state.pendingExercise!!.name,
                weight = state.pendingWeight,
                reps = state.pendingReps,
                rpe = state.pendingRpe,
                error = state.error,
                onWeightChange = viewModel::onPendingWeightChange,
                onRepsChange = viewModel::onPendingRepsChange,
                onRpeChange = viewModel::onPendingRpeChange,
                onLog = viewModel::logSet,
                onDismiss = viewModel::dismissSetEntry
            )
        }
    }
}

@Composable
private fun WorkoutStatsRow(sets: List<WorkoutSet>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatCell(
            value = sets.map { it.exerciseName }.distinct().size.toString(),
            label = "动作"
        )
        StatCell(value = sets.size.toString(), label = "组数")
        StatCell(
            value = "${sets.sumOf { (it.weightKg * it.reps).toDouble() }.roundToInt()} kg",
            label = "总重量"
        )
    }
}

@Composable
private fun StatCell(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF9B9B9B)
        )
    }
}

@Composable
private fun ExerciseGroupCard(exerciseName: String, sets: List<WorkoutSet>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        sets.forEachIndexed { index, set ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "第 ${index + 1} 组",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9B9B9B)
                )
                Text(
                    text = "${set.weightKg} kg × ${set.reps}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color.White
                )
                if (set.rpe != null) {
                    Text(
                        text = "RPE ${set.rpe}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9B9B9B)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExercisePickerContent(
    exercises: List<Exercise>,
    query: String,
    selectedGroup: MuscleGroup,
    onQueryChange: (String) -> Unit,
    onGroupChange: (MuscleGroup) -> Unit,
    onSelect: (Exercise) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "选择动作",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("搜索动作…", color = Color(0xFF6B6B6B)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = workoutFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        // Muscle group filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MuscleGroup.entries.forEach { group ->
                val selected = group == selectedGroup
                Text(
                    text = muscleGroupLabels[group] ?: group.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) Color(0xFF004820) else Color(0xFF9B9B9B),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) Color(0xFF3FFF8B) else Color(0xFF1E1E1E))
                        .clickable { onGroupChange(group) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(exercises) { exercise ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1E1E))
                        .clickable { onSelect(exercise) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(exercise.name, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                        Text(
                            muscleGroupLabels[exercise.muscleGroup] ?: exercise.muscleGroup.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9B9B9B)
                        )
                    }
                }
            }

            if (exercises.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("暂无动作，请搜索或调整分类", color = Color(0xFF6B6B6B))
                    }
                }
            }
        }
    }
}

@Composable
private fun SetEntryContent(
    exerciseName: String,
    weight: String,
    reps: String,
    rpe: String,
    error: String?,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onRpeChange: (String) -> Unit,
    onLog: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("重量 (kg)", color = Color(0xFF9B9B9B)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = workoutFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = reps,
                onValueChange = onRepsChange,
                label = { Text("次数", color = Color(0xFF9B9B9B)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = workoutFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = rpe,
                onValueChange = onRpeChange,
                label = { Text("RPE", color = Color(0xFF9B9B9B)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = workoutFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        if (error != null) {
            Text(error, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onLog,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    brush = Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79))),
                    shape = RoundedCornerShape(14.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("记录这组", color = Color(0xFF004820), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun workoutFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFF3FFF8B),
    unfocusedBorderColor = Color(0xFF2A2A2A),
    cursorColor = Color(0xFF3FFF8B),
    focusedContainerColor = Color(0xFF1A1A1A),
    unfocusedContainerColor = Color(0xFF1A1A1A)
)
