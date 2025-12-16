package com.example.gestorgastos.ui.screens.categories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorgastos.data.ExpenseRepository
import com.example.gestorgastos.domain.model.Category


@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()


    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("¿Eliminar Categoría?") },
            text = { Text("Se eliminará '${categoryToDelete?.name}'. Los gastos asociados NO se borrarán, pero podrían quedar sin icono.") },
            confirmButton = {
                Button(
                    onClick = {
                        ExpenseRepository.deleteCategory(categoryToDelete!!.id)
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                if (category.id != "create_new") {
                    CategoryViewerItem(
                        category = category,
                        onLongClick = { categoryToDelete = category } // Activamos el diálogo
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryViewerItem(category: Category, onLongClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Acción normal */ },
                onLongClick = { onLongClick() } // Acción al mantener pulsado
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono Circular
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(category.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre
            Text(
                text = category.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}