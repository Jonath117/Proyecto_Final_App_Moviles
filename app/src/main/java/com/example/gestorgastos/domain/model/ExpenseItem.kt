package com.example.gestorgastos.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpenseItem(
    val title: String,
    val amount: String,
    val icon: ImageVector,
    val color: Color
)