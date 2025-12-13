package com.example.gestorgastos.navigation

import kotlinx.serialization.Serializable


@Serializable
object Home

@Serializable
object AddExpense

@Serializable
object Categories

@Serializable
object AddCategory

@Serializable
object Reporte

@Serializable
object Profile

@Serializable
data class ExpenseDetail(val id: String)
