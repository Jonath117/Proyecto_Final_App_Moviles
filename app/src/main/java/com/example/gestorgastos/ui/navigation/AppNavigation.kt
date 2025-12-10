package com.example.gestorgastos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gestorgastos.ui.components.BottomNavBar
import com.example.gestorgastos.ui.screens.agregar_gasto.AddGastosScreen
import com.example.gestorgastos.ui.screens.home.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        bottomBar = {
            if (currentRoute != AppScreens.AddGasto.route){
                BottomNavBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute ?: "",
                    onItemClick = { route ->
                        navController.navigate(route) {
                            // Configuración para evitar pilas infinitas al navegar
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
        }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppScreens.Home.route) {
                MainScreen(
                    onFabClick = {
                        navController.navigate(AppScreens.AddGasto.route)
                    }
                )
            }

            composable(AppScreens.AddGasto.route) {
                AddGastosScreen(
                    onBackClick = { navController.popBackStack() } // Pasamos función para volver
                )
            }

            composable(AppScreens.Reporte.route) { /* ScreenReporte() */ }
            composable(AppScreens.Categorias.route) { /* ScreenCategorias() */ }
            composable(AppScreens.Historial.route) { /* ScreenHistorial() */ }
        }
    }
}