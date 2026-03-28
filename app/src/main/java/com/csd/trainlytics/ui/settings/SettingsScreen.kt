package com.csd.trainlytics.ui.settings

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.WeightUnit
import com.csd.trainlytics.domain.model.WeekStartDay

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToExportData: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = Color.White)
            }
            Text(
                "设置",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(Modifier.height(4.dp)) }

                // Account section
                item {
                    SettingsSectionCard {
                        SettingsNavRow(title = "个人资料", subtitle = state.profile.name.ifBlank { "未设置" }, onClick = onNavigateToProfile)
                    }
                }

                // Preferences section
                item {
                    SettingsSectionCard {
                        SettingsToggleRow(
                            title = "24小时制",
                            checked = state.profile.use24HourClock,
                            onCheckedChange = { checked ->
                                viewModel.updateProfile(state.profile.copy(use24HourClock = checked))
                            }
                        )
                        HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
                        SettingsOptionRow(
                            title = "重量单位",
                            value = if (state.profile.weightUnit == WeightUnit.KG) "公斤 (kg)" else "磅 (lbs)",
                            onClick = {
                                val next = if (state.profile.weightUnit == WeightUnit.KG) WeightUnit.LBS else WeightUnit.KG
                                viewModel.updateProfile(state.profile.copy(weightUnit = next))
                            }
                        )
                        HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
                        SettingsOptionRow(
                            title = "每周开始日",
                            value = if (state.profile.weekStartDay == WeekStartDay.MONDAY) "周一" else "周日",
                            onClick = {
                                val next = if (state.profile.weekStartDay == WeekStartDay.MONDAY) WeekStartDay.SUNDAY else WeekStartDay.MONDAY
                                viewModel.updateProfile(state.profile.copy(weekStartDay = next))
                            }
                        )
                    }
                }

                // Notifications section
                item {
                    SettingsSectionCard {
                        SettingsNavRow(title = "通知设置", onClick = onNavigateToNotifications)
                    }
                }

                // Data section
                item {
                    SettingsSectionCard {
                        SettingsNavRow(title = "导出数据", onClick = onNavigateToExportData)
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A1A1A))
    ) {
        content()
    }
}

@Composable
private fun SettingsNavRow(title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = Color(0xFF9B9B9B))
            }
        }
        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF6B6B6B))
    }
}

@Composable
private fun SettingsOptionRow(title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF9B9B9B))
    }
}

@Composable
private fun SettingsToggleRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF004820),
                checkedTrackColor = Color(0xFF3FFF8B),
                uncheckedThumbColor = Color(0xFF9B9B9B),
                uncheckedTrackColor = Color(0xFF333333)
            )
        )
    }
}
