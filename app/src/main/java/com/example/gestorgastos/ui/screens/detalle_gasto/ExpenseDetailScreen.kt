package com.example.gestorgastos.ui.screens.detalle_gasto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorgastos.data.ExpenseRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    viewModel: ExpenseDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    val expense = viewModel.expense
    val LightPurpleBg = Color(0xFFF3E5F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        if (expense == null) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando o no encontrado...", color = Color.Gray)
            }
        } else {

            val category = ExpenseRepository.getCategoryByName(expense.categoryName)
            val icon = category?.icon ?: Icons.Default.Category
            val iconColor = category?.color ?: Color.Black

            Column(modifier = Modifier.padding(24.dp)) {

                Spacer(modifier = Modifier.height(20.dp))

                DetailLabel("Categoría")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = expense.categoryName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Fecha
                DetailLabel("Fecha")
                Text(
                    text = formatDateFull(expense.date), // "20 de diciembre de 2025"
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Detalle
                DetailLabel("Detalle")
                Text(
                    text = expense.title, // "Taxi al aeropuerto"
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Monto
                DetailLabel("Monto")
                Text(
                    text = String.format("%.2f", expense.amount), // "30.00"
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
            }
        }
    }
}

// Componente pequeño para el título gris ("Categoría", "Fecha"...)
@Composable
fun DetailLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

// Formateador de fecha largo (ej: "20 de diciembre de 2025")
fun formatDateFull(millis: Long): String {
    val formatter = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return formatter.format(Date(millis))
}