package com.example.gestorgastos.ui.screens.agregar_gasto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gestorgastos.ui.components.HomeHeader


@Composable
fun AddGastosScreen(onBackClick: () -> Unit = {}) {
    val LightPurpleBg = Color(0xFFF3E5F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HomeHeader("Agregar Gasto", LightPurpleBg)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onBackClick() },
            modifier = Modifier.padding(16.dp)
        )
        {
            Text("Volver / Cancelar")
        }
    }
}