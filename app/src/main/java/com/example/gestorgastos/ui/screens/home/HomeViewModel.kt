package com.example.gestorgastos.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.gestorgastos.domain.model.ExpenseItem
import dagger.hilt.android.lifecycle.HiltViewModel // <--- Importante para @HiltViewModel
import javax.inject.Inject // <--- Importante para @Inject



@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val expenses = listOf(
        ExpenseItem("Transporte", "50.00", Icons.Default.DirectionsBus, Color(0xFF2196F3)),
        ExpenseItem("Salud", "20.00", Icons.Default.Favorite, Color(0xFFF44336))
    )
}
