package com.example.gestorgastos.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Importante para instanciar el VM
import com.example.gestorgastos.domain.model.ExpenseItem
import com.example.gestorgastos.ui.components.ExpenseItemCard
import com.example.gestorgastos.ui.components.MyTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
    onExpenseClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val expenses by viewModel.expenses.collectAsState()

    val CardBg = Color(0xFFEDE7F6)
    val PurpleAccent = Color(0xFF6A1B9A)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = CardBg,
                contentColor = PurpleAccent,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Gasto")
            }
        },
        containerColor = Color.White
    ) { innerPadding ->

        // Contenido Principal
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay gastos registrados", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(expenses) { expense ->
                        ExpenseItemCard(
                            item = expense,
                            backgroundColor = CardBg,
                            onClick = {onExpenseClick(expense.id)}
                        )
                    }
                }
            }
        }
    }
}

 //Componente Local para pintar cada item de la lista
//@Composable
//fun ExpenseItemCard(item: ExpenseItem, backgroundColor: Color) {
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = backgroundColor),
//        elevation = CardDefaults.cardElevation(0.dp),
//        modifier = Modifier.fillMaxWidth().height(70.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                // Icono (Podrías hacerlo dinámico según la categoría)
//                Icon(
//                    imageVector = Icons.Default.MoneyOff, // Icono genérico
//                    contentDescription = null,
//                    tint = Color(0xFF5E35B1),
//                    modifier = Modifier.size(32.dp)
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Column {
//                    Text(
//                        text = item.title,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = item.categoryName,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.Gray
//                    )
//                }
//            }
//            // Precio
//            Text(
//                text = "$${item.amount}",
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = Color.Black
//            )
//        }
//    }
//}