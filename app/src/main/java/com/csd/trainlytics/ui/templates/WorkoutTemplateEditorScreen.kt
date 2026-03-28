package com.csd.trainlytics.ui.templates

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.TemplateExercise

@Composable
fun WorkoutTemplateEditorScreen(
    onBack: () -> Unit,
    viewModel: WorkoutTemplateEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF3FFF8B),
        unfocusedBorderColor = Color(0xFF3A3A3A),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color(0xFF3FFF8B),
        focusedLabelColor = Color(0xFF3FFF8B),
        unfocusedLabelColor = Color(0xFF9B9B9B)
    )

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
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = Color.White)
                }
                Text(
                    if (state.templateName.isBlank()) "新建训练模板" else state.templateName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            item {
                OutlinedTextField(
                    value = state.templateName,
                    onValueChange = { viewModel.setName(it) },
                    label = { Text("模板名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors
                )
            }

            item {
                OutlinedTextField(
                    value = state.note,
                    onValueChange = { viewModel.setNote(it) },
                    label = { Text("备注（可选）") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = fieldColors
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "训练动作",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White
                    )
                    IconButton(onClick = { viewModel.showExercisePicker() }) {
                        Icon(Icons.Filled.Add, contentDescription = "添加动作", tint = Color(0xFF3FFF8B))
                    }
                }
            }

            if (state.exercises.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A1A1A))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "点击 + 添加训练动作",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            itemsIndexed(state.exercises) { index, exercise ->
                TemplateExerciseRow(
                    exercise = exercise,
                    onSetsChange = { sets -> viewModel.updateExerciseSets(index, sets) },
                    onRepsChange = { reps -> viewModel.updateExerciseReps(index, reps) },
                    onRemove = { viewModel.removeExercise(index) },
                    fieldColors = fieldColors
                )
            }

            item {
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { viewModel.save() },
                    enabled = state.templateName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(
                            brush = if (state.templateName.isNotBlank())
                                Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79)))
                            else
                                Brush.linearGradient(listOf(Color(0xFF333333), Color(0xFF333333))),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "保存模板",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = if (state.templateName.isNotBlank()) Color(0xFF004820) else Color(0xFF666666)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    // Exercise picker sheet
    if (state.showExercisePicker) {
        ExercisePickerSheet(
            query = state.exerciseSearchQuery,
            results = state.searchResults,
            onQueryChange = { viewModel.setSearchQuery(it) },
            onSelect = { viewModel.addExercise(it) },
            onDismiss = { viewModel.hideExercisePicker() }
        )
    }
}

@Composable
private fun TemplateExerciseRow(
    exercise: TemplateExercise,
    onSetsChange: (Int) -> Unit,
    onRepsChange: (Int) -> Unit,
    onRemove: () -> Unit,
    fieldColors: androidx.compose.material3.TextFieldColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1A))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                exercise.exerciseName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "移除", tint = Color(0xFF666666), modifier = Modifier.size(16.dp))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = exercise.targetSets.toString(),
                onValueChange = { it.toIntOrNull()?.let(onSetsChange) },
                label = { Text("组数") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = fieldColors
            )
            OutlinedTextField(
                value = exercise.targetReps.toString(),
                onValueChange = { it.toIntOrNull()?.let(onRepsChange) },
                label = { Text("次数") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = fieldColors
            )
        }
    }
}

@Composable
private fun ExercisePickerSheet(
    query: String,
    results: List<com.csd.trainlytics.domain.model.Exercise>,
    onQueryChange: (String) -> Unit,
    onSelect: (com.csd.trainlytics.domain.model.Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color(0xFF1C1C1C))
                .clickable(enabled = false) {}
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "选择动作",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color.White)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("搜索动作...", color = Color(0xFF666666)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3FFF8B),
                    unfocusedBorderColor = Color(0xFF3A3A3A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF3FFF8B)
                )
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                itemsIndexed(results) { index, exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(exercise) }
                            .padding(horizontal = 4.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(exercise.name, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            if (exercise.muscleGroup != com.csd.trainlytics.domain.model.MuscleGroup.ALL) {
                                Text(
                                    exercise.muscleGroup.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF9B9B9B)
                                )
                            }
                        }
                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color(0xFF3FFF8B), modifier = Modifier.size(18.dp))
                    }
                    if (index < results.lastIndex) {
                        HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
