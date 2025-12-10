package com.example.gestorgastos.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gestorgastos.domain.model.ExpenseItem
import androidx.compose.material.icons.filled.DirectionsBus
import com.example.gestorgastos.ui.components.ExpenseCard
import com.example.gestorgastos.ui.components.HomeHeader

@Composable
fun MainScreen(modifier: Modifier = Modifier, onFabClick: () -> Unit) {
    val LightPurpleBg = Color(0xFFF3E5F5)
    val CardBg = Color(0xFFEDE7F6)
    val PurpleAccent = Color(0xFF6A1B9A)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {onFabClick()},
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
            HomeHeader("Home",LightPurpleBg)

            Spacer(modifier = Modifier.height(20.dp))


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

