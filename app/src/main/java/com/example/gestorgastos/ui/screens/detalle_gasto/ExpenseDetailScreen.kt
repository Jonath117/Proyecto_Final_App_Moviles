package com.example.gestorgastos.ui.screens.detalle_gasto

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // <--- IMPORTANTE
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // <--- IMPORTANTE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gestorgastos.data.ExpenseRepository
import com.example.gestorgastos.ui.components.MyButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    viewModel: ExpenseDetailViewModel = viewModel(),
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    val expense = viewModel.expense
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    // Estado para el scroll
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (expense == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando o no encontrado...", color = Color.Gray)
            }
        } else {
            val category = ExpenseRepository.getCategoryByName(expense.categoryName)
            val icon = category?.icon ?: Icons.Default.Category
            val iconColor = category?.color ?: Color.Black


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {

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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Fecha
                DetailLabel("Fecha")
                Text(
                    text = formatDateFull(expense.date),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Detalle
                DetailLabel("Detalle")
                Text(
                    text = expense.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Monto
                DetailLabel("Monto")
                Text(
                    text = String.format("%.2f", expense.amount),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Recibo / Fotos
                if (expense.imageUris.isNotEmpty()) {
                    DetailLabel("Comprobante / Recibo")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        expense.imageUris.forEach { uriString ->
                            Box(
                                modifier = Modifier
                                    .height(120.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                    .clickable { selectedImageUri = uriString }
                            ) {
                                AsyncImage(
                                    model = uriString,
                                    contentDescription = "Foto del recibo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        if (expense.imageUris.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp)) // Espacio antes de los botones

                // BOTONES DE ACCIÓN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    MyButton(
                        text = "Eliminar",
                        onClick = {
                            ExpenseRepository.deleteExpense(expenseId)
                            onBackClick()
                        },
                        icon = Icons.Default.Delete,
                        containerColor = Color(0xFFEF5350),
                        modifier = Modifier.weight(1f),
                        enabled = true,

                    )

                    MyButton(
                        text = "Editar",
                        onClick = { onEditClick(expenseId) },
                        icon = Icons.Default.Edit,
                        containerColor = Color(0xFF7E57C2),
                        modifier = Modifier.weight(1f),
                        enabled = true
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // DIALOGO DE IMAGEN
            if (selectedImageUri != null) {
                Dialog(
                    onDismissRequest = { selectedImageUri = null },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Imagen completa",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )

                        IconButton(
                            onClick = { selectedImageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

fun formatDateFull(millis: Long): String {
    val formatter = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    formatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}