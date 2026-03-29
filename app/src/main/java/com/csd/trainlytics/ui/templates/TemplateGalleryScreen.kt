package com.csd.trainlytics.ui.templates

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.MealTemplateWithItems
import com.csd.trainlytics.domain.model.WorkoutTemplateWithExercises
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun TemplateGalleryScreen(
    onNavigateToWorkoutEditor: (Long) -> Unit,
    onNavigateToMealEditor: (Long) -> Unit = {},
    viewModel: TemplateViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreateWorkoutDialog by remember { mutableStateOf(false) }
    var showCreateMealDialog by remember { mutableStateOf(false) }
    var newTemplateName by remember { mutableStateOf("") }

    if (showCreateWorkoutDialog) {
        AlertDialog(
            onDismissRequest = { showCreateWorkoutDialog = false },
            title = { Text("新建训练模板") },
            text = {
                OutlinedTextField(
                    value = newTemplateName,
                    onValueChange = { newTemplateName = it },
                    label = { Text("模板名称") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        cursorColor = GradientStart
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTemplateName.isNotBlank()) {
                            viewModel.createWorkoutTemplate(newTemplateName) { id ->
                                showCreateWorkoutDialog = false
                                newTemplateName = ""
                                onNavigateToWorkoutEditor(id)
                            }
                        }
                    }
                ) { Text("创建", color = GradientStart) }
            },
            dismissButton = {
                TextButton(onClick = { showCreateWorkoutDialog = false; newTemplateName = "" }) { Text("取消") }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    if (showCreateMealDialog) {
        AlertDialog(
            onDismissRequest = { showCreateMealDialog = false },
            title = { Text("新建饮食模板") },
            text = {
                OutlinedTextField(
                    value = newTemplateName,
                    onValueChange = { newTemplateName = it },
                    label = { Text("模板名称") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        cursorColor = GradientStart
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTemplateName.isNotBlank()) {
                            viewModel.createMealTemplate(newTemplateName) { id ->
                                showCreateMealDialog = false
                                newTemplateName = ""
                                onNavigateToMealEditor(id)
                            }
                        }
                    }
                ) { Text("创建", color = GradientStart) }
            },
            dismissButton = {
                TextButton(onClick = { showCreateMealDialog = false; newTemplateName = "" }) { Text("取消") }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newTemplateName = ""
                    if (selectedTab == 0) showCreateWorkoutDialog = true
                    else showCreateMealDialog = true
                },
                containerColor = GradientStart,
                contentColor = androidx.compose.ui.graphics.Color(0xFF00210B)
            ) {
                Icon(Icons.Filled.Add, null)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "模板库",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            TabRow(selectedTabIndex = selectedTab, containerColor = SurfaceContainerHigh) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("训练模板", modifier = Modifier.padding(vertical = 12.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("饮食模板", modifier = Modifier.padding(vertical = 12.dp))
                }
            }
            Spacer(Modifier.height(12.dp))

            if (selectedTab == 0) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(state.workoutTemplates, key = { it.template.id }) { template ->
                        WorkoutTemplateCard(
                            template = template,
                            onClick = { onNavigateToWorkoutEditor(template.template.id) },
                            onDelete = { viewModel.deleteWorkoutTemplate(template.template.id) }
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(GradientStart.copy(alpha = 0.1f))
                                .clickable { newTemplateName = ""; showCreateWorkoutDialog = true }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Add, null, tint = GradientStart)
                                Spacer(Modifier.width(8.dp))
                                Text("新建训练模板", color = GradientStart, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    if (state.mealTemplates.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("暂无饮食模板", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(state.mealTemplates, key = { it.template.id }) { template ->
                            MealTemplateCard(
                                template = template,
                                onClick = { onNavigateToMealEditor(template.template.id) },
                                onDelete = { viewModel.deleteMealTemplate(template.template.id) }
                            )
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(GradientStart.copy(alpha = 0.1f))
                                .clickable { newTemplateName = ""; showCreateMealDialog = true }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Add, null, tint = GradientStart)
                                Spacer(Modifier.width(8.dp))
                                Text("新建饮食模板", color = GradientStart, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutTemplateCard(
    template: WorkoutTemplateWithExercises,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    BentoCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        backgroundColor = SurfaceContainerLow
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GradientStart.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.FitnessCenter, null, tint = GradientStart, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    template.template.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${template.exercises.size} 个动作 · ${template.totalSets} 组",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                if (template.template.usageCount > 0) {
                    Text(
                        "已完成 ${template.template.usageCount} 次",
                        color = GradientStart,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun MealTemplateCard(
    template: MealTemplateWithItems,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    BentoCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        backgroundColor = SurfaceContainerLow
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GradientStart.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.LocalDining, null, tint = GradientStart, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    template.template.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${template.totalCalories.toInt()} kcal",
                    color = GradientStart,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                if (template.totalProteinG > 0f) {
                    Text(
                        "蛋白质 ${template.totalProteinG.toInt()}g · 碳水 ${template.totalCarbsG.toInt()}g · 脂肪 ${template.totalFatG.toInt()}g",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
