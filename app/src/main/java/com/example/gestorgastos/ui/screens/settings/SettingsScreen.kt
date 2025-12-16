package com.example.gestorgastos.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    Text(text = "Vista de setings", color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
}
