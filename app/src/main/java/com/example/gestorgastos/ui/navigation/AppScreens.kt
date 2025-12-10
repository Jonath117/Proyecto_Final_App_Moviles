package com.example.gestorgastos.ui.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.gestorgastos.domain.model.BottomNavItem

sealed class AppScreens(val route: String) {
    object Home : AppScreens("home")
    object Reporte : AppScreens("report")
    object Categorias : AppScreens("categories")
    object Historial : AppScreens("history")
    object AddGasto : AppScreens("add_gasto")
}

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, AppScreens.Home.route),
    BottomNavItem("Reporte", Icons.Default.Description, AppScreens.Reporte.route),
    BottomNavItem("Categorias", Icons.Default.GridView, AppScreens.Categorias.route),
    BottomNavItem("Historial", Icons.Default.History, AppScreens.Historial.route)
)