package com.csd.trainlytics.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExportDataScreen(onBack: () -> Unit) {
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
                "导出数据",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            item {
                Text(
                    "选择要导出的数据类型",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9B9B9B)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1A1A1A)),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    ExportOptionRow(
                        title = "训练记录",
                        subtitle = "导出所有训练 session 和组数数据 (.csv)",
                        buttonLabel = "导出",
                        onClick = { /* TODO: implement CSV export */ }
                    )
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
                    ExportOptionRow(
                        title = "饮食记录",
                        subtitle = "导出所有饮食记录和营养数据 (.csv)",
                        buttonLabel = "导出",
                        onClick = { /* TODO: implement CSV export */ }
                    )
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
                    ExportOptionRow(
                        title = "身体数据",
                        subtitle = "导出体重、体脂等记录 (.csv)",
                        buttonLabel = "导出",
                        onClick = { /* TODO: implement CSV export */ }
                    )
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
                    ExportOptionRow(
                        title = "全部数据",
                        subtitle = "导出所有数据的完整压缩包 (.zip)",
                        buttonLabel = "导出全部",
                        onClick = { /* TODO: implement full export */ }
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1A1A1A))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "关于数据导出",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFF9B9B9B)
                    )
                    Text(
                        "· 导出文件将保存到设备下载目录\n· CSV 文件可用 Excel 或 Numbers 打开\n· 数据仅存储在本地设备，不上传至云端",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ExportOptionRow(
    title: String,
    subtitle: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF9B9B9B))
        }
        Spacer(Modifier.size(8.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(buttonLabel, color = Color(0xFF3FFF8B), style = MaterialTheme.typography.bodySmall)
        }
    }
}
