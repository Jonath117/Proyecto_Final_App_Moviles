package com.example.gestorgastos.ui.screens.agregar_gasto

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gestorgastos.ui.components.CategoryGridItem
import com.example.gestorgastos.ui.components.LabeledTextField
import com.example.gestorgastos.ui.screens.add_expense.AddExpenseViewModel
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.forEach
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.util.TimeZone
import android.Manifest
import androidx.compose.ui.platform.LocalContext

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

    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(2) // Max 2 fotos
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.onImagesSelected(uris)
        }
    }

    // 2. Permiso Launcher (Determina cuál pedir según versión de Android)
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si da permiso, abrimos la galería
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            Toast.makeText(context, "Se requiere permiso para adjuntar fotos", Toast.LENGTH_SHORT).show()
        }
    }


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
            keyboardType = KeyboardType.Decimal,
            error = if (viewModel.hasInteracted) viewModel.amountError else null
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
                    .background(Color(0xFFdddddd))
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


        Text("Adjuntar Recibo (Máx 2)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (viewModel.selectedImages.size < 2) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F0F0))
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .clickable {
                            permissionLauncher.launch(permissionToRequest)
                        }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = Color.Gray)
                        Text("Subir", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            viewModel.selectedImages.forEach { uri ->
                Box(modifier = Modifier.size(80.dp)) {
                    // Imagen
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    )
                    // Botón X para borrar
                    IconButton(
                        onClick = { viewModel.removeImage(uri) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                            .background(Color.White.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Borrar", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        LabeledTextField(
            label = "Detalle",
            value = viewModel.detail,
            onValueChange = { viewModel.onDetailChange(it) },
            placeholder = "Opcional"
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            viewModel.errorMessage != null -> {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            viewModel.isSuccess -> {
                Text(
                    text = "¡Gasto creado exitosamente!",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveExpense()
                if(viewModel.isFormValid()){
                    onSaveSuccess()
                }
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

        Spacer(modifier = Modifier.height(20.dp))
    }
}


fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd-MM\nyyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}