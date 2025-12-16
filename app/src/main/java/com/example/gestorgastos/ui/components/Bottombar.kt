package com.example.gestorgastos.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.gestorgastos.navigation.BottomNavItem

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onItemClick: (Any) -> Unit
) {
    val SoftLav = Color(0xFFECE4F4)
    val SelectedIconColor = Color(0xFF4A148C)
    val IndicatorColor = Color(0xFFD1C4E9)

    NavigationBar(
        containerColor = SoftLav,
        contentColor = SelectedIconColor,
        modifier = Modifier.shadow(elevation = 25.dp, shape = RectangleShape),
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
                    indicatorColor = IndicatorColor,
                    selectedIconColor = SelectedIconColor,
                    selectedTextColor = SelectedIconColor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}