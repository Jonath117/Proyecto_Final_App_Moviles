package com.example.gestorgastos.navigation

import kotlinx.serialization.Serializable


@Serializable
object Home

@Serializable
data class AddExpense(val expenseId: String? = null)

@Serializable
object Categories

@Serializable
object AddCategory

@Serializable
object Reporteria

@Serializable
object Settings


@Serializable
data class ExpenseDetail(val id: String)
