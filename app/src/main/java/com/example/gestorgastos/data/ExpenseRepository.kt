package com.example.gestorgastos.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import com.example.gestorgastos.domain.model.Category
import com.example.gestorgastos.domain.model.ExpenseItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object ExpenseRepository {

    private val _expenses = MutableStateFlow<List<ExpenseItem>>(emptyList())
    val expenses: StateFlow<List<ExpenseItem>> = _expenses.asStateFlow()

    fun addExpense(expense: ExpenseItem) {
        _expenses.update { it + expense }
    }

    private val _categories = MutableStateFlow<List<Category>>(
        listOf(
            Category("1", "Transporte", Icons.Default.DirectionsBus, Color(0xFF4FC3F7)),
            Category("2", "Salud", Icons.Default.Favorite, Color(0xFFEF5350)),
            Category("3", "Casa", Icons.Default.Home, Color(0xFF66BB6A)),
            Category("4", "Regalos", Icons.Default.CardGiftcard, Color(0xFF8D6E63)),
            Category("5", "Comida", Icons.Default.Restaurant, Color(0xFFFFA726)),
            Category("6", "Familia", Icons.Default.FamilyRestroom, Color(0xFFAB47BC)),
            Category("7", "Ocio", Icons.Default.Weekend, Color(0xFF78909C))
        )
    )
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun addCategory(category: Category) {
        val currentList = _categories.value.toMutableList()
        currentList.add(category)
        _categories.value = currentList
    }

    fun getCategoryByName(name: String): Category? {
        return _categories.value.find { it.name == name }
    }

    fun deleteExpense(expenseId: String) {
        val currentList = _expenses.value.toMutableList()
        currentList.removeIf { it.id == expenseId }
        _expenses.value = currentList
    }

    // ACTUALIZAR GASTO
    fun updateExpense(updatedItem: ExpenseItem) {
        val currentList = _expenses.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            currentList[index] = updatedItem
            _expenses.value = currentList
        }
    }

    // OBTENER GASTO POR ID
    fun getExpenseById(id: String): ExpenseItem? {
        return _expenses.value.find { it.id == id }
    }

    // ELIMINAR CATEGORÍA
    fun deleteCategory(categoryId: String) {
        val currentList = _categories.value.toMutableList()
        currentList.removeIf { it.id == categoryId }
        _categories.value = currentList
    }
}