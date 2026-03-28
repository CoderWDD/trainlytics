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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplate
import kotlin.math.roundToInt

@Composable
fun TemplateGalleryScreen(
    onNavigateToWorkoutTemplateEditor: (Long) -> Unit,
    onNavigateToMealTemplateEditor: (Long) -> Unit,
    viewModel: TemplateGalleryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Header with add button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "模板库",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (state.selectedTab == TemplateTab.WORKOUT)
                        onNavigateToWorkoutTemplateEditor(-1L)
                    else
                        onNavigateToMealTemplateEditor(-1L)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "新建模板", tint = Color(0xFF3FFF8B))
            }
        }

        // Tab row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TemplateTab.values().forEach { tab ->
                val isSelected = state.selectedTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFF1E3320) else Color(0xFF1A1A1A))
                        .clickable { viewModel.selectTab(tab) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tab == TemplateTab.WORKOUT) "训练模板" else "饮食模板",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal),
                        color = if (isSelected) Color(0xFF3FFF8B) else Color(0xFF9B9B9B)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
            state.selectedTab == TemplateTab.WORKOUT -> {
                if (state.workoutTemplates.isEmpty()) {
                    EmptyTemplateHint("暂无训练模板", "点击右上角「+」创建第一个模板")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.workoutTemplates) { template ->
                            WorkoutTemplateCard(
                                template = template,
                                onClick = { onNavigateToWorkoutTemplateEditor(template.id) },
                                onDelete = { viewModel.deleteWorkoutTemplate(template.id) }
                            )
                        }
                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
            else -> {
                if (state.mealTemplates.isEmpty()) {
                    EmptyTemplateHint("暂无饮食模板", "点击右上角「+」创建第一个模板")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.mealTemplates) { template ->
                            MealTemplateCard(
                                template = template,
                                onClick = { onNavigateToMealTemplateEditor(template.id) },
                                onDelete = { viewModel.deleteMealTemplate(template.id) }
                            )
                        }
                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTemplateHint(title: String, subtitle: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF9B9B9B))
            Spacer(Modifier.height(8.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF6B6B6B))
        }
    }
}

@Composable
private fun WorkoutTemplateCard(
    template: WorkoutTemplate,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                template.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            val exerciseCount = template.exercises.size
            Text(
                "${exerciseCount} 个动作 · 已用 ${template.usageCount} 次",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9B9B9B)
            )
            if (template.note.isNotBlank()) {
                Text(template.note, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B6B6B))
            }
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Color(0xFF6B6B6B))
        }
    }
}

@Composable
private fun MealTemplateCard(
    template: MealTemplate,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                template.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Text(
                "${template.totalCalories.roundToInt()} kcal · ${template.items.size} 项",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9B9B9B)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("蛋白 ${template.totalProteinG.roundToInt()}g", style = MaterialTheme.typography.labelSmall, color = Color(0xFF6B9BFF))
                Text("碳水 ${template.totalCarbsG.roundToInt()}g", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFACC15))
                Text("脂肪 ${template.totalFatG.roundToInt()}g", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF6B6B))
            }
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Color(0xFF6B6B6B))
        }
    }
}
