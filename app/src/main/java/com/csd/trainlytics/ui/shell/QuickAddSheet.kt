package com.csd.trainlytics.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.csd.trainlytics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddSheet(
    onDismiss: () -> Unit,
    onNavigateToRecordBodyStats: () -> Unit,
    onNavigateToRecordMeal: () -> Unit,
    onNavigateToActiveWorkout: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF141414),
        tonalElevation = 0.dp
    ) {
        QuickAddSheetContent(
            onDismiss = onDismiss,
            onNavigateToRecordBodyStats = onNavigateToRecordBodyStats,
            onNavigateToRecordMeal = onNavigateToRecordMeal,
            onNavigateToActiveWorkout = onNavigateToActiveWorkout
        )
    }
}

@Composable
private fun QuickAddSheetContent(
    onDismiss: () -> Unit,
    onNavigateToRecordBodyStats: () -> Unit,
    onNavigateToRecordMeal: () -> Unit,
    onNavigateToActiveWorkout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "快速添加",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        QuickAddItem(
            iconResId = R.drawable.ic_today_filled,
            iconBackground = Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79))),
            iconTint = Color(0xFF004820),
            title = "记录体重",
            subtitle = "记录今日体重和体脂率",
            onClick = { onDismiss(); onNavigateToRecordBodyStats() }
        )

        QuickAddItem(
            iconResId = R.drawable.ic_insights_filled,
            iconBackground = Brush.linearGradient(listOf(Color(0xFF3B82F6), Color(0xFF2563EB))),
            iconTint = Color.White,
            title = "记录饮食",
            subtitle = "添加一餐或单个食物",
            onClick = { onDismiss(); onNavigateToRecordMeal() }
        )

        QuickAddItem(
            iconResId = R.drawable.ic_templates_filled,
            iconBackground = Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF9333EA))),
            iconTint = Color.White,
            title = "开始训练",
            subtitle = "从模板开始或自由训练",
            onClick = { onDismiss(); onNavigateToActiveWorkout() }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun QuickAddItem(
    iconResId: Int,
    iconBackground: Brush,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E1E))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconResId),
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9B9B9B)
            )
        }
    }
}
