package com.example.gestorgastos.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // <--- Necesario
import androidx.compose.runtime.getValue     // <--- Necesario
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.gestorgastos.data.AppThemeMode
import com.example.gestorgastos.data.ThemeManager // <--- Tu manager
import com.example.gestorgastos.navigation.BottomNavItem
import com.example.gestorgastos.ui.theme.PurplePrimaryDark

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onItemClick: (Any) -> Unit
) {
    val themeMode by ThemeManager.themeMode.collectAsState()

    val isDark = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val containerColor = if (isDark) Color(0xFF121212) else Color(0xFFECE4F4)
    val selectedIconColor = if (isDark) PurplePrimaryDark else Color(0xFF4A148C)
    val indicatorColor = if (isDark) Color(0xFF4A148C).copy(alpha = 0.3f) else Color(0xFFD1C4E9)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Transparent

    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = borderColor
        )

        NavigationBar(
            containerColor = containerColor,
            contentColor = selectedIconColor,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->

                val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                    selected = isSelected,
                    onClick = { onItemClick(item.route) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = indicatorColor,
                        selectedIconColor = if (isDark) Color.Black else selectedIconColor,
                        selectedTextColor = selectedIconColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}