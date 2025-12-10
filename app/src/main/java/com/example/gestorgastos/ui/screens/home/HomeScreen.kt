package com.example.gestorgastos.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestorgastos.domain.model.BottomNavItem
import com.example.gestorgastos.domain.model.ExpenseItem

import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import com.example.gestorgastos.ui.components.BottomNavBar
import com.example.gestorgastos.ui.components.ExpenseCard
import com.example.gestorgastos.ui.components.HomeHeader

@Composable
fun MainScreen(modifier: Modifier) {
    val LightPurpleBg = Color(0xFFF3E5F5) // Fondo general
    val CardBg = Color(0xFFEDE7F6)        // Fondo de tarjetas
    val PurpleAccent = Color(0xFF6A1B9A)  // Color de iconos activos

    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Reporte", Icons.Default.Description, "report"),
        BottomNavItem("Categorias", Icons.Default.GridView, "categories"),
        BottomNavItem("Historial", Icons.Default.History, "history")
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = navItems,
                currentRoute = "home",
                onItemClick = {route  -> }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = CardBg,
                contentColor = PurpleAccent,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Fondo blanco detrás de todo
                .padding(paddingValues)
        ) {
            HomeHeader(LightPurpleBg)

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de Items
            val expenses = listOf(
                ExpenseItem("Transporte", "50.00", Icons.Default.DirectionsBus, Color(0xFF4FC3F7)),
                ExpenseItem("Salud", "20.00", Icons.Filled.AddCircle, Color(0xFFEF5350))
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(expenses) { expense ->
                    ExpenseCard(expense, CardBg)
                }
            }
        }
    }
}

// COMPONENTE: Cabecera "HOME"


// COMPONENTE: Tarjeta de Gasto
