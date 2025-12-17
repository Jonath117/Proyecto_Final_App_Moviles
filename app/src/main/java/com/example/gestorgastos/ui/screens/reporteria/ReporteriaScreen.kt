package com.example.gestorgastos.ui.screens.reporteria

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorgastos.domain.model.CategoryReportItem
import com.example.gestorgastos.ui.components.PieChart

@Composable
fun ReporteriaScreen(
    viewModel: ReporteriaViewModel = viewModel()
){
    val reportData by viewModel.reportData.collectAsState()
    val totalSpent by viewModel.totalSpent.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        if (reportData.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay datos para mostrar", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp)
            ) {
                // ITEM 1: El Gráfico
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PieChart(
                            data = reportData,
                            totalAmount = totalSpent,
                        )
                    }
                }

                // ITEM 2: Título de la lista
                item {
                    Text(
                        text = "Detalle por Categoría",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                // ITEMS: Lista de categorías con porcentaje
                items(reportData) { item ->
                    CategoryRow(item)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryRow(item: CategoryReportItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Bolita de color
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(item.color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Nombre y porcentaje
            Column {
                Text(text = item.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                Text(
                    text = "${String.format("%.1f", item.percentage * 100)}%",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
        }
        // Monto
        Text(
            text = "$${String.format("%.2f", item.totalAmount)}",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}