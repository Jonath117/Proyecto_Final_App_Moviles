package com.example.gestorgastos.ui.screens.agregar_categoria

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorgastos.ui.components.LabeledTextField
import com.example.gestorgastos.ui.components.MyButton
import com.example.gestorgastos.ui.screens.add_category.AddCategoryViewModel

@Composable
fun AddCategoryScreen(
    viewModel: AddCategoryViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val LightPurpleBg = Color(0xFFF3E5F5)
    val PurpleButtonColor = Color(0xFF7E57C2)

    LaunchedEffect(viewModel.isSaved) {
        if (viewModel.isSaved) {
            onSaveSuccess()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(viewModel.selectedColor.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = viewModel.selectedIcon,
                        contentDescription = null,
                        tint = viewModel.selectedColor,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (viewModel.categoryName.isEmpty()) "Nombre" else viewModel.categoryName,
                    fontWeight = FontWeight.Bold,
                    color = viewModel.selectedColor
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        LabeledTextField(
            label = "Nombre de Categoría",
            value = viewModel.categoryName,
            onValueChange = { viewModel.onNameChange(it) },
            placeholder = "Ej: Gimnasio, Viajes...",
            error = if (viewModel.hasInteracted) viewModel.nameError else null
        )

        Spacer(modifier = Modifier.height(32.dp))


        // 3. SELECTOR DE COLORES
        Text("Elige un Color", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(viewModel.availableColors) { color ->
                ColorSelectorItem(
                    color = color,
                    isSelected = color == viewModel.selectedColor,
                    onClick = { viewModel.onColorSelected(color) }
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // 4. SELECTOR DE ICONOS
        Text("Elige un Icono", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(300.dp)
        ) {
            items(viewModel.availableIcons) { icon ->
                IconSelectorItem(
                    icon = icon,
                    isSelected = icon == viewModel.selectedIcon,
                    selectedColor = viewModel.selectedColor,
                    onClick = { viewModel.onIconSelected(icon) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))


        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            MyButton(
                text = "Cancelar",
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
                containerColor = Color.LightGray,
                enabled = true
            )

            MyButton(
                text = if (viewModel.isLoading) "Guardando..." else "Guardar",
                enabled = !viewModel.isLoading && viewModel.categoryName.isNotBlank(),
                onClick = {
                    viewModel.saveCategory()
                },
                icon = Icons.Default.Check,
                containerColor = PurpleButtonColor,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



@Composable
fun ColorSelectorItem(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(3.dp, Color.Black.copy(alpha = 0.5f), CircleShape)
                else Modifier
            )
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center).size(24.dp)
            )
        }
    }
}

@Composable
fun IconSelectorItem(
    icon: ImageVector,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) selectedColor.copy(alpha = 0.2f) else Color(0xFFF5F5F5))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) selectedColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) selectedColor else Color.Gray
        )
    }
}