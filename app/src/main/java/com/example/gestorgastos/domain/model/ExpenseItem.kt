package com.example.gestorgastos.domain.model

data class ExpenseItem(
    val id: String,
    val title: String,
    val amount: Double,
    val categoryName: String,
    val date: Long

)