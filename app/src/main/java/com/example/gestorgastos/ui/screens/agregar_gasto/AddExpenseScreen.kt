package com.example.gestorgastos.ui.screens.agregar_gasto

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestorgastos.ui.components.CategoryGridItem
import com.example.gestorgastos.ui.components.LabeledTextField
import com.example.gestorgastos.ui.screens.add_expense.AddExpenseViewModel
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.forEach
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    onCreateCategoryClick: () -> Unit
) {
    val purpleButtonColor = Color(0xFF7E57C2)

    val scrollState = rememberScrollState()

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = viewModel.dateMillis
    )

    val categoriesList by viewModel.categories.collectAsState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onDateChange(millis)
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {

        LabeledTextField(
            label = "Monto",
            value = viewModel.amount,
            onValueChange = { newValue ->
                val filtered = newValue
                    .replace(",", ".")
                    .filter { it.isDigit() || it == '.' }

                viewModel.onAmountChange(filtered)
            },
            placeholder = "0.00",
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Categorías",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        val chunkedCategories = categoriesList.chunked(4)

        Column(modifier = Modifier.fillMaxWidth()) {
            chunkedCategories.forEach { rowItems -> // Iteramos sobre los trozos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { category ->
                        CategoryGridItem(
                            category = category,
                            isSelected = viewModel.selectedCategory?.id == category.id,
                            onSelect = {
                                if (category.id == "create_new") {
                                    onCreateCategoryClick()
                                } else {
                                    viewModel.onCategorySelected(category)
                                }
                            }
                        )
                    }

                    // Rellenamos huecos vacíos para mantener la alineación
                    repeat(4 - rowItems.size) {
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = formatDate(viewModel.dateMillis),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Seleccionar fecha",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LabeledTextField(
            label = "Detalle",
            value = viewModel.detail,
            onValueChange = { viewModel.onDetailChange(it) },
            placeholder = "Opcional"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.saveExpense()
                onSaveSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purpleButtonColor),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Añadir gasto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp)) // Espacio final
    }
}


fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd-MM\nyyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}