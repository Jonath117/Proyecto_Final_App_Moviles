package com.example.gestorgastos.domain.model

import androidx.compose.ui.graphics.Color

data class CategoryReportItem(
    val name: String,
    val totalAmount: Double,
    val color: Color,
    val percentage: Float
)