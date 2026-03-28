package com.csd.trainlytics.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.csd.trainlytics.R
import com.csd.trainlytics.core.designsystem.GlassNavBackground
import com.csd.trainlytics.core.navigation.NavRoutes

data class BottomNavItem(
    val route: String,
    val label: String,
    val iconResId: Int,
    val iconFilledResId: Int
)

val bottomNavItems = listOf(
    BottomNavItem(NavRoutes.Today.route, "今日", R.drawable.ic_today_outline, R.drawable.ic_today_filled),
    BottomNavItem(NavRoutes.History.route, "历史", R.drawable.ic_history_outline, R.drawable.ic_history_filled),
    BottomNavItem(NavRoutes.Insights.route, "分析", R.drawable.ic_insights_outline, R.drawable.ic_insights_filled),
    BottomNavItem(NavRoutes.Templates.route, "模板", R.drawable.ic_templates_outline, R.drawable.ic_templates_filled),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainShell(
    navController: NavController,
    onNavigateToRecordBodyStats: () -> Unit = {},
    onNavigateToRecordMeal: () -> Unit = {},
    onNavigateToActiveWorkout: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showQuickAdd by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuickAdd = true },
                shape = CircleShape,
                containerColor = Color(0xFF3FFF8B),
                contentColor = Color(0xFF005D2C),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "快速添加")
            }
        },
        bottomBar = {
            GlassBottomNav(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            content()
        }
    }

    if (showQuickAdd) {
        QuickAddSheet(
            onDismiss = { showQuickAdd = false },
            onNavigateToRecordBodyStats = {
                showQuickAdd = false
                onNavigateToRecordBodyStats()
            },
            onNavigateToRecordMeal = {
                showQuickAdd = false
                onNavigateToRecordMeal()
            },
            onNavigateToActiveWorkout = {
                showQuickAdd = false
                onNavigateToActiveWorkout()
            }
        )
    }
}

@Composable
private fun GlassBottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GlassNavBackground)
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                BottomNavItemView(
                    item = item,
                    selected = selected,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItemView(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = Color(0xFF3FFF8B)
    val inactiveColor = Color(0xFF9B9B9B)
    val color = if (selected) activeColor else inactiveColor

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (selected) Modifier.background(Color(0xFF1A1A1A)) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (selected) item.iconFilledResId else item.iconResId
            ),
            contentDescription = item.label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
