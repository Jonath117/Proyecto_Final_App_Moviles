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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    viewModel: ExpenseDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    // Cargamos el gasto al iniciar la pantalla
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
        // HEADER REUTILIZADO (Título "Detalle Transacción")
        // Nota: Asegúrate de que HomeHeader soporte título personalizado o usa un TopAppBar simple aquí si prefieres
        // Aquí usaré un bloque simple para igualar tu imagen si HomeHeader es fijo

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(LightPurpleBg)
//                .padding(top = 40.dp, bottom = 20.dp), // Ajuste para barra de estado
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "Detalle Transacción",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
//        }

        if (expense == null) {
            // Estado de carga o error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando o no encontrado...", color = Color.Gray)
            }
        } else {
            // DATOS DEL GASTO
            Column(modifier = Modifier.padding(24.dp)) {

                Spacer(modifier = Modifier.height(20.dp))

                // 1. Categoría
                DetailLabel("Categoría")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Círculo gris con icono (simulado)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Aquí podrías buscar el icono real de la categoría si lo tuvieras guardado
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = expense.categoryName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Fecha
                DetailLabel("Fecha")
                Text(
                    text = formatDateFull(expense.date), // "20 de diciembre de 2025"
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Detalle
                DetailLabel("Detalle")
                Text(
                    text = expense.title, // "Taxi al aeropuerto"
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Monto
                DetailLabel("Monto")
                Text(
                    text = String.format("%.2f", expense.amount), // "30.00"
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
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
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

// Formateador de fecha largo (ej: "20 de diciembre de 2025")
fun formatDateFull(millis: Long): String {
    val formatter = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return formatter.format(Date(millis))
}