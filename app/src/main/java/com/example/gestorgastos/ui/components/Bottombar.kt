package com.example.gestorgastos.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.gestorgastos.domain.model.BottomNavItem

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    currentRoute: String = "home",
    onItemClick: (String) -> Unit
) {
    val LightPurpleBg = Color(0xFFF3E5F5)

    NavigationBar(containerColor = LightPurpleBg) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                selected = currentRoute == item.route,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}