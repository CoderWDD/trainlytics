package com.csd.trainlytics.ui.body

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
import androidx.compose.material.icons.filled.Close
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.ui.components.GradientButton
import com.csd.trainlytics.ui.components.RulerSlider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun RecordBodyStatsScreen(
    onDismiss: () -> Unit,
    viewModel: BodyViewModel = hiltViewModel()
) {
    val state by viewModel.entryState.collectAsState()
    val now = LocalDateTime.now()
    val fmt = DateTimeFormatter.ofPattern("M月d日 HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Filled.Close, null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "记录体征",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = now.format(fmt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        RulerSlider(
            value = state.weightKgFloat,
            min = 30f,
            max = 200f,
            step = 0.1f,
            label = "体重",
            unit = "kg",
            onValueChange = viewModel::updateWeightFloat,
            majorTickEvery = 10
        )

        Spacer(Modifier.height(16.dp))

        RulerSlider(
            value = state.bodyFatFloat,
            min = 3f,
            max = 60f,
            step = 0.1f,
            label = "体脂率",
            unit = "%",
            onValueChange = viewModel::updateBodyFatFloat,
            majorTickEvery = 5
        )

        Spacer(Modifier.height(16.dp))

        RulerSlider(
            value = state.waistCmFloat,
            min = 50f,
            max = 150f,
            step = 0.5f,
            label = "腰围",
            unit = "cm",
            onValueChange = viewModel::updateWaistFloat,
            majorTickEvery = 10
        )

        Spacer(Modifier.height(28.dp))

        GradientButton(
            text = if (state.isSaving) "保存中..." else "保存记录",
            onClick = { viewModel.saveRecord { onDismiss() } },
            enabled = !state.isSaving
        )
        Spacer(Modifier.height(80.dp))
    }
}
