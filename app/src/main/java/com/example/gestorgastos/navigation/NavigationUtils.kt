package com.example.gestorgastos.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gestorgastos.navigation.Home
import com.example.gestorgastos.navigation.Categories

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)