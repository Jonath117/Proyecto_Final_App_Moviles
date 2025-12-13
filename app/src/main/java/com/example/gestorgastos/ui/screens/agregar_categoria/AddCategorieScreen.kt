package com.example.gestorgastos.ui.screens.agregar_categoria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorgastos.ui.components.LabeledTextField
import com.example.gestorgastos.ui.screens.add_category.AddCategoryViewModel

@Composable
fun AddCategoryScreen(
    viewModel: AddCategoryViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val LightPurpleBg = Color(0xFFF3E5F5)
    val PurpleButtonColor = Color(0xFF7E57C2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        LabeledTextField(
            label = "Nombre de Categoría",
            value = viewModel.categoryName,
            onValueChange = { viewModel.onNameChange(it) },
            placeholder = "Ej: Gimnasio, Viajes..."
        )

        Spacer(modifier = Modifier.height(32.dp))


        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar", color = Color.Black)
            }

            Button(
                onClick = {
                    viewModel.saveCategory()
                    onSaveSuccess()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleButtonColor),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar")
            }
        }
    }
}