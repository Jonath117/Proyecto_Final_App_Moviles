package com.example.gestorgastos.ui.screens.add_expense

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.domain.model.ExpenseItem
import com.example.gestorgastos.data.ExpenseRepository
import java.util.UUID
import com.example.gestorgastos.domain.model.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class AddExpenseViewModel : ViewModel() {

    var amount by mutableStateOf("")
        private set
    var detail by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    var dateMillis by mutableStateOf(System.currentTimeMillis())
        private set

    val categories = ExpenseRepository.categories.map { list ->
        list + Category(
            id = "create_new",
            name = "Crear",
            icon = Icons.Default.Add,
            color = Color(0xFFE0E0E0)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onAmountChange(text: String) { amount = text }
    fun onDetailChange(text: String) { detail = text }
    fun onCategorySelected(category: Category) { selectedCategory = category }

    fun saveExpense() {
        if (amount.isNotBlank() && selectedCategory != null) {
            val newItem = ExpenseItem(
                id = UUID.randomUUID().toString(),
                title = detail.ifBlank { selectedCategory!!.name },
                amount = amount.toDoubleOrNull() ?: 0.0,
                categoryName = selectedCategory!!.name,
                date = dateMillis
            )
            ExpenseRepository.addExpense(newItem)

            // Limpiar
            amount = ""
            detail = ""
            selectedCategory = null
        }
    }

    fun onDateChange(newDate: Long) {
        dateMillis = newDate
    }
}