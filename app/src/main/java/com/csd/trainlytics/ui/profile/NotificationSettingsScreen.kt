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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.ui.components.BentoCard
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import com.csd.trainlytics.ui.theme.SurfaceContainerLow

@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

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
                text = "通知设置",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(16.dp))

        BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow, padding = 0.dp) {
            Column {
                NotifSwitchRow(
                    label = "启用通知",
                    sublabel = "开启所有应用提醒",
                    checked = settings.notificationsEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(notificationsEnabled = it)) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "饮食提醒",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow, padding = 0.dp) {
            Column {
                NotifTimeRow(
                    label = "早餐提醒",
                    time = settings.mealReminderBreakfast,
                    enabled = settings.notificationsEnabled
                )
                NotifDivider()
                NotifTimeRow(
                    label = "午餐提醒",
                    time = settings.mealReminderLunch,
                    enabled = settings.notificationsEnabled
                )
                NotifDivider()
                NotifTimeRow(
                    label = "晚餐提醒",
                    time = settings.mealReminderDinner,
                    enabled = settings.notificationsEnabled
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "训练提醒",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BentoCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SurfaceContainerLow, padding = 0.dp) {
            Column {
                NotifTimeRow(
                    label = "训练提醒",
                    time = settings.workoutReminderTime ?: "未设置",
                    enabled = settings.notificationsEnabled
                )
                NotifDivider()
                NotifTimeRow(
                    label = "每周回顾",
                    time = settings.weeklyReviewReminderTime ?: "未设置",
                    enabled = settings.notificationsEnabled
                )
            }
        }

        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun NotifSwitchRow(
    label: String,
    sublabel: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            Text(text = sublabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.background,
                checkedTrackColor = GradientStart,
                uncheckedTrackColor = SurfaceContainerHighest
            )
        )
    }
}

@Composable
private fun NotifTimeRow(label: String, time: String?, enabled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = time ?: "未设置",
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled && time != null) GradientStart else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NotifDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 16.dp)
            .background(SurfaceContainerHighest)
    )
}
