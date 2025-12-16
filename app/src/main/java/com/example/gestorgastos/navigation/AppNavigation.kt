package com.example.gestorgastos.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.gestorgastos.ui.components.BottomNavBar

import com.example.gestorgastos.ui.components.MyTopAppBar
import com.example.gestorgastos.ui.screens.add_expense.AddExpenseViewModel

import com.example.gestorgastos.ui.screens.agregar_gasto.AddExpenseScreen
import com.example.gestorgastos.ui.screens.home.HomeScreen
import com.example.gestorgastos.ui.screens.agregar_categoria.AddCategoryScreen // <--- Asegura este import
import com.example.gestorgastos.ui.screens.categories.CategoriesScreen // <--- Y este también
import com.example.gestorgastos.ui.screens.detalle_gasto.ExpenseDetailScreen
import com.example.gestorgastos.ui.screens.profile.ProfileScreen
import com.example.gestorgastos.ui.screens.reporteria.ReporteriaScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, Home),
        BottomNavItem("Categorías", Icons.Default.GridView, Categories),
        BottomNavItem("Reportes", Icons.Default.PieChart, Reporteria)
    )

    val currentTitle = when {
        currentDestination?.hasRoute<Home>() == true -> "Mis Gastos"
        currentDestination?.hasRoute<AddExpense>() == true -> "Añadir Gasto"
        currentDestination?.hasRoute<Categories>() == true -> "Categorías"
        currentDestination?.hasRoute<AddCategory>() == true -> "Nueva Categoría"
        currentDestination?.hasRoute<ExpenseDetail>() == true -> "Detalle del Gasto"
        currentDestination?.hasRoute<Profile>() == true -> "Perfil"
        currentDestination?.hasRoute<Reporteria>() == true -> "Reporte"
        else -> "Gestor de Gastos"
    }

    val canNavigateBack = navController.previousBackStackEntry != null &&
            currentDestination?.hasRoute<Home>() == false &&
            currentDestination?.hasRoute<Categories>() == false &&
            currentDestination?.hasRoute<Reporteria>() == false

    val showBottomBar = currentDestination?.hasRoute<AddExpense>() == false &&
            currentDestination?.hasRoute<AddCategory>() == false && currentDestination?.hasRoute<Profile>() == false


    Scaffold(
        topBar = {
            MyTopAppBar(
                title = currentTitle,
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() },
                onProfileClick = { navController.navigate(Profile) }

            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    items = navItems,
                    currentDestination = currentDestination,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(Home) { saveState = true }
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
            startDestination = Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Home> {
                HomeScreen(
                    onFabClick = { navController.navigate(AddExpense(null)) },
                    onExpenseClick = { id -> navController.navigate(ExpenseDetail(id = id)) }
                )
            }

            composable<AddExpense> { backStackEntry ->
                val args = backStackEntry.toRoute<AddExpense>()

                val viewModel: AddExpenseViewModel = viewModel()

                LaunchedEffect(args.expenseId) {
                    viewModel.loadExpenseIfEditing(args.expenseId)
                }

                AddExpenseScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() },
                    onCreateCategoryClick = { navController.navigate(AddCategory) }
                )
            }

            composable<Categories> {
                CategoriesScreen()
            }

            composable<AddCategory> {
                AddCategoryScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }

            composable<Profile> {
                ProfileScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable<ExpenseDetail> { backStackEntry ->
                // Extraemos el argumento 'id' de la ruta de forma segura
                val detail: ExpenseDetail = backStackEntry.toRoute()

                ExpenseDetailScreen(
                    expenseId = detail.id,
                    onBackClick = { navController.popBackStack()},
                    onEditClick = { id -> navController.navigate(AddExpense(id)) }
                )
            }

            composable<Reporteria> {
                ReporteriaScreen()
            }
        }
    }
}