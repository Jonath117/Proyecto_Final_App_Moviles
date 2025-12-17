package com.example.gestorgastos.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsSystemDaydream
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorgastos.data.AppThemeMode

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val currentTheme by viewModel.currentTheme.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Apariencia",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Opciones de Tema
            ThemeOptionItem(
                title = "Tema del Sistema",
                icon = Icons.Default.SettingsSystemDaydream,
                isSelected = currentTheme == AppThemeMode.SYSTEM,
                onClick = { viewModel.changeTheme(context, AppThemeMode.SYSTEM) }
            )
            ThemeOptionItem(
                title = "Modo Claro",
                icon = Icons.Default.LightMode,
                isSelected = currentTheme == AppThemeMode.LIGHT,
                onClick = { viewModel.changeTheme(context, AppThemeMode.LIGHT) }
            )
            ThemeOptionItem(
                title = "Modo Oscuro",
                icon = Icons.Default.DarkMode,
                isSelected = currentTheme == AppThemeMode.DARK,
                onClick = { viewModel.changeTheme(context, AppThemeMode.DARK) }
            )
        }
    }
}

@Composable
fun ThemeOptionItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF7E57C2) else Color.Gray,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF7E57C2) else MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            RadioButton(selected = true, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF7E57C2)))
        }
    }
}